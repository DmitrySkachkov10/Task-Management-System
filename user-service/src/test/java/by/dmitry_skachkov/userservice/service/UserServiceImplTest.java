package by.dmitry_skachkov.userservice.service;

import by.dmitry_skachkov.userservice.core.dto.UserDTO;
import by.dmitry_skachkov.userservice.repo.UserRepo;

import by.dmitryskachkov.exception.exceptions.email.EmailAlreadyExistsException;
import by.dmitryskachkov.exception.exceptions.email.InvalidEmailFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepo userRepository;
    private UserDTO validUserDTO;
    private UserDTO invalidEmailUserDTO;

    @BeforeEach
    void setUp() {
        validUserDTO = new UserDTO("test.email@gmail.com", "test_password");
        invalidEmailUserDTO = new UserDTO("badEmailFormat.com", "test_password");
    }


    @Test
    void createUser_withValidEmail_saveUser() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedString");

        userService.createUser(validUserDTO);

        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void createUser_withInvalidEmail_throwException() {
        assertThrows(InvalidEmailFormatException.class, () -> userService.createUser(invalidEmailUserDTO));
    }

    @Test
    void createUser_withAlreadyExistsEmail_throwException() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(validUserDTO));
    }
}