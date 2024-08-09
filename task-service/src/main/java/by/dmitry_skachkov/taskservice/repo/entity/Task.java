package by.dmitry_skachkov.taskservice.repo.entity;

import by.dmitry_skachkov.taskservice.model.Priority;
import by.dmitry_skachkov.taskservice.model.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(schema = "task_service", name = "task")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Task {

    @Id
    private UUID uuid;

    private String description;

    private String header;

    private UUID authorUuid;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Version
    private long version;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TaskPerformer> taskPerformers = new HashSet<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    public Task(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return version == task.version &&
                Objects.equals(uuid, task.uuid) &&
                Objects.equals(description, task.description) &&
                Objects.equals(header, task.header) &&
                Objects.equals(authorUuid, task.authorUuid) &&
                status == task.status &&
                priority == task.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, description, header, authorUuid, status, priority, version);
    }
}
