package by.dmitry_skachkov.taskservice.controller;

import by.dmitry_skachkov.taskservice.core.dto.task.TaskCreateDto;
import by.dmitry_skachkov.taskservice.core.dto.task.UpdateTaskDto;
import by.dmitry_skachkov.taskservice.service.TaskServiceImpl;
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
        return null;
    }

    @GetMapping("/my")
    public ResponseEntity<?> readMy(@RequestParam(name = "page", defaultValue = "1") int page,
                                    @RequestParam(name = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok().body(taskService.getMyTasks(page, size));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> delete(@PathVariable UUID uuid) {
        taskService.delete(uuid);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMyTask(@RequestBody UpdateTaskDto updateTaskDto) {
        taskService.updateTask(updateTaskDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{uuid}/version/{version}")
    private ResponseEntity<?> updateStatus(@PathVariable UUID uuid,
                                           @PathVariable long version,
                                           @RequestParam String status) {
        taskService.changeStatus(status, version, uuid);
        return ResponseEntity.ok().build();
    }
}
