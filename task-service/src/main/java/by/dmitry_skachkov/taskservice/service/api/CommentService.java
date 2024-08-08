package by.dmitry_skachkov.taskservice.service.api;

import java.util.UUID;

public interface CommentService {
    void addComment(String comment, UUID taskUuid);
}
