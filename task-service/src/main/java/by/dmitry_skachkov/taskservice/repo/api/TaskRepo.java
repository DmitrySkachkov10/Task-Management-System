package by.dmitry_skachkov.taskservice.repo.api;

import by.dmitry_skachkov.taskservice.repo.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepo extends JpaRepository<Task, UUID> {

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.taskPerformers tp " +
            "LEFT JOIN FETCH t.comments " +
            "WHERE t.authorUuid = :userUuid OR tp.performerUuid = :userUuid")
    Page<Task> findByAuthorUuidOrPerformerUuid(@Param("userUuid") UUID userUuid, Pageable pageable);

    @EntityGraph(attributePaths = {"comments", "taskPerformers"})
    Optional<Task> findById(UUID id);

    @EntityGraph(attributePaths = {"comments", "taskPerformers"})
    Page<Task> findAll(Pageable pageable);
}
