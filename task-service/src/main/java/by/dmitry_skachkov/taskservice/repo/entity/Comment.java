package by.dmitry_skachkov.taskservice.repo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(schema = "task_service", name = "comment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {

    @Id
    private UUID uuid;

    private String text;

    @Column(name = "commentator_uuid")
    private UUID commentatorUuid;

    @ManyToOne
    @JoinColumn(name = "task_uuid")
    private Task task;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(uuid, comment.uuid) && Objects.equals(text, comment.text) && Objects.equals(commentatorUuid, comment.commentatorUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, text, commentatorUuid);
    }
}
