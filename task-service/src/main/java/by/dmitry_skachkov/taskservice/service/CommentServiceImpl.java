package by.dmitry_skachkov.taskservice.service;

import by.dmitry_skachkov.taskservice.core.dto.comment.CreateCommentDto;
import by.dmitry_skachkov.taskservice.core.dto.task.TaskDto;
import by.dmitry_skachkov.taskservice.core.utils.SecurityUtils;
import by.dmitry_skachkov.taskservice.repo.api.CommentRepo;
import by.dmitry_skachkov.taskservice.repo.entity.Comment;
import by.dmitry_skachkov.taskservice.repo.entity.Task;
import by.dmitry_skachkov.taskservice.service.api.CommentService;
import by.dmitry_skachkov.taskservice.service.api.TaskService;
import by.dmitryskachkov.exception.PerimissionDeniedException;
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

        TaskDto taskDto = taskService.getByUuid(commentDto.getUuid());
        UUID userUuid = SecurityUtils.getAuthenticatedUserUuid();

        boolean isAuthor = taskDto.getAuthorUuid().equals(userUuid.toString());
        boolean isPerformer = taskDto.getPerformersUuid().stream()
                .anyMatch(uuid -> uuid.equals(userUuid));

        if (!isAuthor && !isPerformer) {
            //is not the author or performer
            throw new PerimissionDeniedException("User does not have permission to add comments to this task");
        } else {
            Comment comment = new Comment(UUID.randomUUID(),
                    commentDto.getText(),
                    userUuid,
                    new Task(commentDto.getUuid()));
            commentRepo.save(comment);
        }
    }
}
