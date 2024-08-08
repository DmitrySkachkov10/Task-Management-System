package by.dmitry_skachkov.taskservice.controller;

import by.dmitry_skachkov.taskservice.core.dto.comment.CreateCommentDto;
import by.dmitry_skachkov.taskservice.service.api.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateCommentDto commentDto) {
        commentService.addComment(commentDto);
        return ResponseEntity.ok().build(); //todo create status
    }
}
