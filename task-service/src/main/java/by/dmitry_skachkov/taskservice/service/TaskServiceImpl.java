package by.dmitry_skachkov.taskservice.service;

import by.dmitry_skachkov.taskservice.core.dto.task.PageOfTask;
import by.dmitry_skachkov.taskservice.core.dto.task.TaskCreateDto;
import by.dmitry_skachkov.taskservice.core.dto.task.TaskDto;
import by.dmitry_skachkov.taskservice.core.mapper.TaskConverter;
import by.dmitry_skachkov.taskservice.core.utils.UserAuth;
import by.dmitry_skachkov.taskservice.model.Priority;
import by.dmitry_skachkov.taskservice.model.Status;
import by.dmitry_skachkov.taskservice.repo.api.TaskRepo;
import by.dmitry_skachkov.taskservice.repo.entity.Task;
import by.dmitry_skachkov.taskservice.repo.entity.TaskPerformer;
import by.dmitry_skachkov.taskservice.service.api.TaskService;
import by.dmitryskachkov.exception.exceptions.InvalidUuidException;
import by.dmitryskachkov.exception.exceptions.UnauthorizedActionException;
import by.dmitryskachkov.exception.exceptions.VersionConflictException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;

    private final TaskConverter taskMapper;

    public TaskServiceImpl(TaskRepo taskRepo, TaskConverter taskMapper) {
        this.taskRepo = taskRepo;
        this.taskMapper = taskMapper;
    }

    @Override
    @Transactional
    public void create(TaskCreateDto taskCreate) {

        UUID userUuid = getAuthenticatedUserUuid();

        Task task = Task.builder()
                .uuid(UUID.randomUUID())
                .description(taskCreate.getDescription())
                .header(taskCreate.getHeader())
                .authorUuid(userUuid)
                .status(Status.valueOf(taskCreate.getStatus()))
                .priority(Priority.valueOf(taskCreate.getPriority()))
                .build();

        Set<TaskPerformer> taskPerformerSet = taskCreate.getPerformersUuid()
                .stream()
                .map(uuid -> new TaskPerformer(task.getUuid(), uuid, task))
                .collect(Collectors.toSet());

        task.setTaskPerformers(taskPerformerSet);

        taskRepo.save(task);
    }

    @Override
    public void delete(UUID uuid) {
        taskRepo.deleteById(uuid);
    }


    @Override
    public void updateTask(TaskCreateDto createDto, long version, UUID uuid) {
        UUID userUuid = getAuthenticatedUserUuid();

        Task task = taskRepo.findById(uuid)
                .orElseThrow(() -> new InvalidUuidException("Invalid task UUID: " + uuid));

        if (!task.getAuthorUuid().equals(userUuid)) {
            throw new UnauthorizedActionException("User is not the author of the task");
        }

        if (task.getVersion() != version) {
            throw new VersionConflictException("Version conflict: the task has been updated by another user");
        }

        Set<TaskPerformer> taskPerformerSet = createDto.getPerformersUuid()
                .stream()
                .map(performerUuid -> new TaskPerformer(task.getUuid(), performerUuid, task))
                .collect(Collectors.toSet());

        task.setHeader(createDto.getHeader());
        task.setDescription(createDto.getDescription());
        task.setPriority(Priority.valueOf(createDto.getPriority()));
        task.setStatus(Status.valueOf(createDto.getStatus()));
        task.setTaskPerformers(taskPerformerSet);
        task.setVersion(version);

        taskRepo.save(task);
    }

    @Override
    public PageOfTask getByUserUuid(UUID uuid, int page, int size) {

        final UUID userUuid = (uuid != null) ? uuid : getAuthenticatedUserUuid(); //if you want your own tasks

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Task> taskPage = taskRepo.findByAuthorUuidOrPerformerUuid(userUuid, pageable);

        List<TaskDto> taskDtos = taskPage.getContent()
                .stream()
                .map(task -> convertToTaskDtoWithFlags(task, userUuid))
                .collect(Collectors.toList());

        PageOfTask pageOfTask = new PageOfTask();
        pageOfTask.setPageNumber(taskPage.getNumber());
        pageOfTask.setPageSize(taskPage.getSize());
        pageOfTask.setTotalPages(taskPage.getTotalPages());
        pageOfTask.setTotalElements(taskPage.getTotalElements());
        pageOfTask.setContent(taskDtos);

        return pageOfTask;
    }

    @Override
    public void changeStatus(String status, long version, UUID uuid) {
        UUID userUuid = getAuthenticatedUserUuid();

        Task task = taskRepo.findById(uuid)
                .orElseThrow(() -> new InvalidUuidException("Invalid task UUID: " + uuid));

        if (task.getTaskPerformers().stream().noneMatch(performer -> performer.getPerformerUuid().equals(userUuid))) {
            throw new UnauthorizedActionException("User is not a performer or author of the task");
        }

        if (task.getVersion() != version) {
            throw new VersionConflictException("Version conflict: the task has been updated by another user");
        }

        task.setStatus(Status.valueOf(status));
        task.setVersion(version);

        taskRepo.save(task);
    }


    private TaskDto convertToTaskDtoWithFlags(Task task, UUID userUuid) {
        TaskDto dto = taskMapper.toDto(task);

        boolean isAuthor = task.getAuthorUuid().equals(userUuid);
        boolean isPerformer = task.getTaskPerformers().stream()
                .anyMatch(tp -> tp.getPerformerUuid().equals(userUuid));

        dto.setAuthor(isAuthor);
        dto.setPerformer(!isAuthor && isPerformer);

        return dto;
    }


    private UUID getAuthenticatedUserUuid() {
        UserAuth userAuth = (UserAuth) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return UUID.fromString(userAuth.getUUID());
    }
}
