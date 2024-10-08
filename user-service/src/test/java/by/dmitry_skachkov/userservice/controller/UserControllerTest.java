package by.dmitry_skachkov.userservice.controller;

import by.dmitry_skachkov.userservice.core.dto.UserLogin;
import by.dmitry_skachkov.userservice.core.dto.UserRegistration;
import by.dmitry_skachkov.userservice.service.UserServiceImpl;
import by.dmitryskachkov.exception.exceptions.email.EmailAlreadyExistsException;
import by.dmitryskachkov.exception.exceptions.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    private UserLogin validUserLogin;

    private UserLogin invalidUserLogin;
    private UserRegistration validUserRegistration;
    private UserRegistration invalidEmailUserRegistration;


    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalHandlerController())
                .build();

        validUserRegistration = new UserRegistration("test.email@gmail.com", "test_password");
        invalidEmailUserRegistration = new UserRegistration("badEmailFormat.com", "test_password");
    }

    @Test
    public void registrate_validUser_returnsCreated() throws Exception {

        doNothing().when(userService).createUser(any(UserRegistration.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserRegistration)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).createUser(any(UserRegistration.class));

    }

    @Test
    public void registrate_invalidEmail_throwsInvalidEmailFormatException() throws Exception {

        doThrow(new ValidationException("Invalid email format")).when(userService).createUser(any(UserRegistration.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmailUserRegistration)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid email format"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value("error"));

        verify(userService, times(1)).createUser(any(UserRegistration.class));
    }

    @Test
    public void registrate_emailAlreadyExists_throwsEmailAlreadyExistsException() throws Exception {

        doThrow(new EmailAlreadyExistsException("Email already exists")).when(userService).createUser(any(UserRegistration.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserRegistration)))
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Email already exists"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value("error"));

        verify(userService, times(1)).createUser(any(UserRegistration.class));
    }

    @Test
    public void logIn_validCredentials_returnsToken() throws Exception {
        String token = "token";

        when(userService.logIn(any(UserLogin.class))).thenReturn(token);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserLogin)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(token));

        verify(userService, times(1)).logIn(any(UserLogin.class));
    }

    @Test
    public void logIn_invalidCredentials_throwsValidationException() throws Exception {

        when(userService.logIn(any(UserLogin.class))).thenThrow(new ValidationException("Invalid email or password"));

        mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUserLogin)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid email or password"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value("error"));

        verify(userService, times(1)).logIn(any(UserLogin.class));
    }
}
