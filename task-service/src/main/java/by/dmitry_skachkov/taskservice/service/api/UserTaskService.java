package by.dmitry_skachkov.taskservice.service.api;

import by.dmitry_skachkov.taskservice.core.dto.PageOfTask;
import by.dmitry_skachkov.taskservice.core.dto.TaskCreateDto;
import by.dmitry_skachkov.taskservice.core.dto.UpdateTaskDto;

import java.util.UUID;

public interface UserTaskService {

    void create(TaskCreateDto task);

    void delete(UUID uuid);

    PageOfTask getMyTasks(int page, int size);

    void updateTask(UpdateTaskDto updateTaskDto);

    PageOfTask getByUserUuid(UUID uuid, int page, int size);

}
