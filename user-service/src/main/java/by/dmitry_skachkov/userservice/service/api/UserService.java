package by.dmitry_skachkov.userservice.service.api;

import by.dmitry_skachkov.userservice.core.dto.UserDto;
import by.dmitry_skachkov.userservice.core.dto.UserLogin;
import by.dmitry_skachkov.userservice.core.dto.UserRegistration;


public interface UserService {

    void createUser(UserRegistration userRegistration);

    String logIn(UserLogin userLogin);

    UserDto myInfo();

}
