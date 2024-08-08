package by.dmitry_skachkov.userservice.service.api;

import by.dmitry_skachkov.userservice.core.dto.PageOfUser;
import by.dmitry_skachkov.userservice.core.dto.UserDto;
import by.dmitry_skachkov.userservice.core.dto.UserLogin;
import by.dmitry_skachkov.userservice.core.dto.UserRegistration;

import java.util.UUID;


public interface UserService {

    void createUser(UserRegistration userRegistration);

    String logIn(UserLogin userLogin);

    UserDto myInfo();

    PageOfUser getUsers (int page, int size);

    UserDto getUser(UUID uuid);
}
