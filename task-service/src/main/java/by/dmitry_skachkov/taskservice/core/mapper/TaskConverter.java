package by.dmitry_skachkov.taskservice.core.mapper;

import by.dmitry_skachkov.taskservice.core.dto.task.TaskDto;
import by.dmitry_skachkov.taskservice.model.Priority;
import by.dmitry_skachkov.taskservice.model.Status;
import by.dmitry_skachkov.taskservice.repo.entity.Comment;
import by.dmitry_skachkov.taskservice.repo.entity.Task;
import by.dmitry_skachkov.taskservice.repo.entity.TaskPerformer;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TaskConverter {


    public TaskDto toDto(Task task) {
        TaskDto dto = new TaskDto();

        dto.setUuid(task.getUuid().toString());
        dto.setHeader(task.getHeader());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus().name());
        dto.setPriority(task.getPriority().name());
        dto.setAuthorUuid(task.getAuthorUuid().toString());
        dto.setVersion(task.getVersion());

        Set<UUID> performerUuids = task.getTaskPerformers().stream()
                .map(TaskPerformer::getPerformerUuid)
                .collect(Collectors.toSet());
        dto.setPerformersUuid(performerUuids);

        Set<String> commentsText = task.getComments().stream()
                .map(Comment::getText)
                .collect(Collectors.toSet());
        dto.setComments(commentsText);

        return dto;
    }


    public Task toEntity(TaskDto dto) {
        Task task = new Task();

        task.setUuid(UUID.fromString(dto.getUuid()));
        task.setHeader(dto.getHeader());
        task.setDescription(dto.getDescription());
        task.setStatus(Status.valueOf(dto.getStatus()));
        task.setPriority(Priority.valueOf(dto.getPriority()));
        task.setAuthorUuid(UUID.fromString(dto.getAuthorUuid()));
        task.setVersion(dto.getVersion());


        Set<TaskPerformer> performers = dto.getPerformersUuid().stream()
                .map(uuid -> {
                    TaskPerformer performer = new TaskPerformer();
                    performer.setPerformerUuid(uuid);
                    performer.setTaskUuid(task.getUuid());
                    performer.setTask(task);
                    return performer;
                })
                .collect(Collectors.toSet());
        task.setTaskPerformers(performers);


        Set<Comment> comments = dto.getComments().stream()
                .map(text -> {
                    Comment comment = new Comment();
                    comment.setText(text);
                    comment.setTask(task);
                    return comment;
                })
                .collect(Collectors.toSet());
        task.setComments(comments);

        return task;
    }
}
