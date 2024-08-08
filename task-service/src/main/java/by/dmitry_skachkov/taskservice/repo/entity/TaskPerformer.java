package by.dmitry_skachkov.taskservice.repo.entity;

import by.dmitry_skachkov.taskservice.model.TaskPerformerId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(schema = "task_service", name = "task_performer")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@IdClass(TaskPerformerId.class)
public class TaskPerformer {

    @Id
    @Column(name = "task_uuid")
    private UUID taskUuid;

    @Id
    private UUID performerUuid;

    @ManyToOne
    @JoinColumn(name = "task_uuid", insertable = false, updatable = false)
    private Task task;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskPerformer that = (TaskPerformer) o;
        return Objects.equals(taskUuid, that.taskUuid) && Objects.equals(performerUuid, that.performerUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskUuid, performerUuid);
    }
}
