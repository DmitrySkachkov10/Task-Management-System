package by.dmitry_skachkov.taskservice.service.api;

import by.dmitry_skachkov.taskservice.core.dto.comment.CreateCommentDto;

public interface CommentService {
    void addComment(CreateCommentDto commentDto);
}
