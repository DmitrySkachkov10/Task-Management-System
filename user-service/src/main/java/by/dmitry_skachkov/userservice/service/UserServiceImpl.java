package by.dmitry_skachkov.userservice.service;

import by.dmitry_skachkov.userservice.core.dto.UserDTO;
import by.dmitry_skachkov.userservice.entity.UserEntity;
import by.dmitry_skachkov.userservice.repo.UserRepo;
import by.dmitry_skachkov.userservice.service.api.UserService;
import jakarta.transaction.Transactional;

import java.util.UUID;

public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public void createUser(UserDTO userDTO) {
        if (userRepo.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("email already exists"); //todo create custom exception
        }
        userRepo.save(new UserEntity(
                UUID.randomUUID(),
                userDTO.getEmail(),
                userDTO.getPassword())
        );
    }

    @Override
    public void logIn(UserDTO userDTO) {
        
    }

    @Override
    public UserDTO myInfo() {
        return null;
    }
}
