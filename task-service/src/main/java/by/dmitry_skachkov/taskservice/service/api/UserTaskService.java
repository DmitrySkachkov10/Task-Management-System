package by.dmitry_skachkov.taskservice.service.api;

import by.dmitry_skachkov.taskservice.core.dto.task.PageOfTask;
import by.dmitry_skachkov.taskservice.core.dto.task.TaskCreateDto;
import by.dmitry_skachkov.taskservice.core.dto.task.TaskDto;
import by.dmitry_skachkov.taskservice.core.dto.task.TaskFilterDto;

import java.util.UUID;

public interface UserTaskService {

    void create(TaskCreateDto task);

    void delete(UUID uuid);

    void updateTask(TaskCreateDto createDto, long version, UUID uuid);

    PageOfTask getTasks(TaskFilterDto taskFilterDto);

    TaskDto getByUuid(UUID uuid);

}
