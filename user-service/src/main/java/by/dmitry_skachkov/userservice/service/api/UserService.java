package by.dmitry_skachkov.userservice.service.api;

import by.dmitry_skachkov.userservice.core.dto.UserDTO;


public interface UserService {

    void createUser(UserDTO userDTO);

    String logIn(UserDTO userDTO);

}
