package by.dmitry_skachkov.taskservice.service;

import by.dmitry_skachkov.taskservice.core.dto.task.PageOfTask;
import by.dmitry_skachkov.taskservice.core.dto.task.TaskCreateDto;
import by.dmitry_skachkov.taskservice.core.dto.task.UpdateTaskDto;
import by.dmitry_skachkov.taskservice.repo.api.TaskRepo;
import by.dmitry_skachkov.taskservice.service.api.TaskService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;

    public TaskServiceImpl(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    @Override
    public void create(TaskCreateDto task) {

    }

    @Override
    public void delete(UUID uuid) {

    }

    @Override
    public PageOfTask getMyTasks(int page, int size) {
        return null;
    }

    @Override
    public void updateTask(UpdateTaskDto updateTaskDto) {

    }

    @Override
    public PageOfTask getByUserUuid(UUID uuid, int page, int size) {
        return null;
    }

    @Override
    public void changeStatus(String status, long version, UUID uuid) {

    }
}
