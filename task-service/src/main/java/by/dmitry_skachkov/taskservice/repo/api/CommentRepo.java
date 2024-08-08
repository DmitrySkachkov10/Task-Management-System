package by.dmitry_skachkov.taskservice.repo.api;

import by.dmitry_skachkov.taskservice.repo.entity.Comment;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Registered
public interface CommentRepo extends JpaRepository<Comment, UUID> {
}
