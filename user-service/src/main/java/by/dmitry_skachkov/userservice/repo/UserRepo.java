package by.dmitry_skachkov.userservice.repo;

import by.dmitry_skachkov.userservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, UUID> {
    boolean existsByEmail(String email);

    UserEntity findByEmail( String email);
}
