package by.dmitry_skachkov.taskservice.service;

import by.dmitry_skachkov.taskservice.core.dto.comment.CreateCommentDto;
import by.dmitry_skachkov.taskservice.core.dto.task.TaskDto;
import by.dmitry_skachkov.taskservice.core.utils.SecurityUtils;
import by.dmitry_skachkov.taskservice.repo.api.CommentRepo;
import by.dmitry_skachkov.taskservice.repo.entity.Comment;
import by.dmitry_skachkov.taskservice.service.api.TaskService;
import by.dmitryskachkov.exception.exceptions.PerimissionDeniedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private TaskService taskService;

    @Mock
    private CommentRepo commentRepo;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private CommentServiceImpl commentService;


    @Test
    void addComment_success() {
        UUID userUuid = UUID.randomUUID();
        UUID taskUuid = UUID.randomUUID();
        String commentText = "This is a comment";

        CreateCommentDto commentDto = new CreateCommentDto();
        commentDto.setUuid(taskUuid);
        commentDto.setText(commentText);

        TaskDto taskDto = new TaskDto();
        taskDto.setAuthorUuid(userUuid.toString());
        taskDto.setPerformersUuid(Set.of(userUuid));

        when(securityUtils.getAuthenticatedUserUuid()).thenReturn(userUuid);
        when(taskService.getByUuid(taskUuid)).thenReturn(taskDto);

        commentService.addComment(commentDto);

        verify(commentRepo, times(1)).save(any(Comment.class));

    }

    @Test
    void addComment_permissionDenied() {
        UUID userUuid = UUID.randomUUID();
        UUID taskUuid = UUID.randomUUID();
        String commentText = "This is a comment";

        CreateCommentDto commentDto = new CreateCommentDto();
        commentDto.setUuid(taskUuid);
        commentDto.setText(commentText);

        TaskDto taskDto = new TaskDto();
        taskDto.setAuthorUuid(UUID.randomUUID().toString());
        taskDto.setPerformersUuid(Set.of());  // No performers

        when(securityUtils.getAuthenticatedUserUuid()).thenReturn(userUuid);
        when(taskService.getByUuid(taskUuid)).thenReturn(taskDto);

        assertThrows(PerimissionDeniedException.class, () -> commentService.addComment(commentDto));
    }

    @Test
    void addComment_success_as_performer() {
        UUID userUuid = UUID.randomUUID();
        UUID taskUuid = UUID.randomUUID();
        String commentText = "This is a comment";

        CreateCommentDto commentDto = new CreateCommentDto();
        commentDto.setUuid(taskUuid);
        commentDto.setText(commentText);

        TaskDto taskDto = new TaskDto();
        taskDto.setAuthorUuid(UUID.randomUUID().toString());
        taskDto.setPerformersUuid(Set.of(userUuid));

        when(securityUtils.getAuthenticatedUserUuid()).thenReturn(userUuid);
        when(taskService.getByUuid(taskUuid)).thenReturn(taskDto);

        commentService.addComment(commentDto);

        verify(commentRepo, times(1)).save(any(Comment.class));
    }
}