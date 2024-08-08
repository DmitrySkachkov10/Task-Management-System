package by.dmitry_skachkov.userservice.controller;

import by.dmitry_skachkov.userservice.core.dto.UserDto;
import by.dmitry_skachkov.userservice.core.dto.UserLogin;
import by.dmitry_skachkov.userservice.core.dto.UserRegistration;
import by.dmitry_skachkov.userservice.service.api.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<Void> registrate(@RequestBody UserRegistration userRegistration) {
        userService.createUser(userRegistration);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> logIn(@RequestBody UserLogin userRegistration) {
        return ResponseEntity.ok(userService.logIn(userRegistration));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> myInfo() {
        return ResponseEntity.ok().body(userService.myInfo());
    }
}
