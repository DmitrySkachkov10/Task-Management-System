package by.dmitry_skachkov.taskservice.service;

import by.dmitry_skachkov.taskservice.core.dto.task.TaskCreateDto;
import by.dmitry_skachkov.taskservice.core.utils.SecurityUtils;
import by.dmitry_skachkov.taskservice.repo.api.TaskRepo;
import by.dmitry_skachkov.taskservice.repo.entity.Task;
import by.dmitry_skachkov.taskservice.core.mapper.TaskConverter;
import by.dmitryskachkov.exception.exceptions.InvalidUuidException;
import by.dmitryskachkov.exception.exceptions.UnauthorizedActionException;
import by.dmitryskachkov.exception.exceptions.VersionConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepo taskRepo;

    @Mock
    private TaskConverter taskMapper;

    @Mock
    private SecurityUtils securityUtils;
    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void createTask_success() {

        UUID userUuid = UUID.randomUUID();
        TaskCreateDto createDto = new TaskCreateDto();
        createDto.setHeader("Test Task");
        createDto.setDescription("Task Description");
        createDto.setPriority("HIGH");
        createDto.setStatus("PENDING");
        createDto.setPerformersUuid(Set.of(UUID.randomUUID()));

        when(securityUtils.getAuthenticatedUserUuid()).thenReturn(userUuid);

        taskService.create(createDto);


        verify(taskRepo, times(1)).save(any(Task.class));
    }

    @Test
    void deleteTask_success() {
        UUID taskUuid = UUID.randomUUID();

        taskService.delete(taskUuid);

        verify(taskRepo, times(1)).deleteById(taskUuid);
    }


    @Test
    void updateTask_success() {
        UUID taskUuid = UUID.randomUUID();
        UUID userUuid = UUID.randomUUID();

        Task existingTask = Task.builder()
                .uuid(taskUuid)
                .authorUuid(userUuid)
                .version(1L)
                .build();

        TaskCreateDto createDto = new TaskCreateDto();
        createDto.setHeader("Updated Task");
        createDto.setDescription("Updated Description");
        createDto.setPriority("LOW");
        createDto.setStatus("IN_PROGRESS");
        createDto.setPerformersUuid(Set.of(UUID.randomUUID()));

        when(securityUtils.getAuthenticatedUserUuid()).thenReturn(userUuid);
        when(taskRepo.findById(taskUuid)).thenReturn(Optional.of(existingTask));

        taskService.updateTask(createDto, 1L, taskUuid);

        verify(taskRepo, times(1)).save(existingTask);
    }

    @Test
    void updateTask_versionConflict_throwsException() {

        UUID taskUuid = UUID.randomUUID();
        UUID userUuid = UUID.randomUUID();

        Task existingTask = Task.builder()
                .uuid(taskUuid)
                .authorUuid(userUuid)
                .version(2L)
                .build();

        TaskCreateDto createDto = new TaskCreateDto();

        when(securityUtils.getAuthenticatedUserUuid()).thenReturn(userUuid);
        when(taskRepo.findById(taskUuid)).thenReturn(Optional.of(existingTask));


        assertThrows(VersionConflictException.class, () -> {
            taskService.updateTask(createDto, 1L, taskUuid);
        });
    }

    @Test
    void updateTask_invalidUuid() {
        UUID userUuid = UUID.randomUUID();
        UUID taskUuid = UUID.randomUUID();
        when(securityUtils.getAuthenticatedUserUuid()).thenReturn(userUuid);
        when(taskRepo.findById(taskUuid)).thenReturn(Optional.empty());

        TaskCreateDto updateDto = new TaskCreateDto();
        updateDto.setHeader("Updated Task");

        assertThrows(InvalidUuidException.class, () -> taskService.updateTask(updateDto, 1L, taskUuid));
    }

    @Test
    void updateTask_unauthorized() {
        UUID userUuid = UUID.randomUUID();
        UUID taskUuid = UUID.randomUUID();
        Task existingTask = new Task();
        existingTask.setUuid(taskUuid);
        existingTask.setAuthorUuid(UUID.randomUUID());
        existingTask.setVersion(1L);
        when(taskRepo.findById(taskUuid)).thenReturn(Optional.of(existingTask));
        when(securityUtils.getAuthenticatedUserUuid()).thenReturn(userUuid);

        TaskCreateDto updateDto = new TaskCreateDto();
        updateDto.setHeader("Updated Task");

        assertThrows(UnauthorizedActionException.class, () -> taskService.updateTask(updateDto, 1L, taskUuid));
    }

    @Test
    void changeStatus_unauthorized() {
        UUID userUuid = UUID.randomUUID();
        UUID taskUuid = UUID.randomUUID();
        Task existingTask = new Task();
        existingTask.setUuid(taskUuid);
        existingTask.setAuthorUuid(UUID.randomUUID());
        existingTask.setVersion(1L);
        when(taskRepo.findById(taskUuid)).thenReturn(Optional.of(existingTask));
        when(securityUtils.getAuthenticatedUserUuid()).thenReturn(userUuid);

        assertThrows(UnauthorizedActionException.class, () -> taskService.changeStatus("COMPLETED", 1L, taskUuid));
    }

}
