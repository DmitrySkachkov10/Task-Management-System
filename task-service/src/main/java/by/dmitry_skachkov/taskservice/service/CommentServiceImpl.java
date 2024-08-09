package by.dmitry_skachkov.taskservice.service;

import by.dmitry_skachkov.taskservice.core.dto.comment.CreateCommentDto;
import by.dmitry_skachkov.taskservice.core.utils.SecurityUtils;
import by.dmitry_skachkov.taskservice.repo.api.CommentRepo;
import by.dmitry_skachkov.taskservice.repo.entity.Comment;
import by.dmitry_skachkov.taskservice.repo.entity.Task;
import by.dmitry_skachkov.taskservice.service.api.CommentService;
import by.dmitry_skachkov.taskservice.service.api.TaskService;
import by.dmitryskachkov.exception.exceptions.InvalidUuidException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {

    private final TaskService taskService;
    private final CommentRepo commentRepo;

    public CommentServiceImpl(TaskService taskService, CommentRepo commentRepo) {
        this.taskService = taskService;
        this.commentRepo = commentRepo;
    }

    @Override
    public void addComment(CreateCommentDto commentDto) {

        if (taskService.getByUuid(commentDto.getTaskUuid()) == null) {
            throw new InvalidUuidException("invalid uuid " + commentDto.getTaskUuid());
        }

        Task task = new Task();
        task.setUuid(commentDto.getTaskUuid());

        UUID userUuid = SecurityUtils.getAuthenticatedUserUuid();
        Comment comment = new Comment(UUID.randomUUID(),
                commentDto.getText(),
                userUuid,
                task);

        commentRepo.save(comment);
    }
}
