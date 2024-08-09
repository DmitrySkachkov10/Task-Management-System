package by.dmitry_skachkov.taskservice.controller;

import by.dmitry_skachkov.taskservice.core.dto.task.PageOfTask;
import by.dmitry_skachkov.taskservice.core.dto.task.TaskCreateDto;
import by.dmitry_skachkov.taskservice.core.dto.task.TaskFilterDto;
import by.dmitry_skachkov.taskservice.core.resolver.TaskFilterResolver;
import by.dmitry_skachkov.taskservice.service.TaskServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskServiceImpl taskService;

    public TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody TaskCreateDto task) {
        taskService.create(task);
        return new ResponseEntity<>("Task has been created", HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<PageOfTask> getTasks(TaskFilterDto taskFilterDto) {
        return ResponseEntity.ok().body(taskService.getTasks(taskFilterDto));
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<?> delete(@PathVariable UUID uuid) {
        taskService.delete(uuid);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{uuid}/version/{version}")
    public ResponseEntity<?> updateMyTask(@RequestBody TaskCreateDto taskCreateDto,
                                          @PathVariable long version,
                                          @PathVariable UUID uuid) {
        taskService.updateTask(taskCreateDto, version, uuid);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/status/{uuid}/version/{version}")
    private ResponseEntity<?> updateStatus(@PathVariable UUID uuid,
                                           @PathVariable long version,
                                           @RequestParam String status) {
        taskService.changeStatus(status, version, uuid);
        return ResponseEntity.ok().build();
    }

}
