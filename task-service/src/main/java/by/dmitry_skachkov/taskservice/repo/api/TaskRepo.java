package by.dmitry_skachkov.taskservice.repo.api;

import by.dmitry_skachkov.taskservice.repo.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepo extends JpaRepository<Task, UUID> {

    @Query("SELECT t FROM Task t LEFT JOIN t.taskPerformers tp " +
            "WHERE t.authorUuid = :userUuid OR tp.performerUuid = :userUuid")
    Page<Task> findByAuthorUuidOrPerformerUuid(@Param("userUuid") UUID userUuid, Pageable pageable);

}
