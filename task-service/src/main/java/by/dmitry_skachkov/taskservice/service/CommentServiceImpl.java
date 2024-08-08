package by.dmitry_skachkov.taskservice.service;

import by.dmitry_skachkov.taskservice.repo.api.CommentRepo;
import by.dmitry_skachkov.taskservice.service.api.CommentService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepo commentRepo;

    public CommentServiceImpl(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    @Override
    public void addComment(String comment, UUID taskUuid) {

    }
}
