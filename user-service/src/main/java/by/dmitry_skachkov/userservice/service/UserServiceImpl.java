package by.dmitry_skachkov.userservice.service;

import by.dmitry_skachkov.userservice.core.dto.UserDTO;
import by.dmitry_skachkov.userservice.core.utils.EmailValidator;
import by.dmitry_skachkov.userservice.core.utils.JwtTokenHandler;
import by.dmitry_skachkov.userservice.core.utils.UserAuth;
import by.dmitry_skachkov.userservice.repo.entity.UserEntity;
import by.dmitry_skachkov.userservice.repo.UserRepo;
import by.dmitry_skachkov.userservice.service.api.UserService;
import by.dmitryskachkov.exception.exceptions.ValidationException;
import by.dmitryskachkov.exception.exceptions.email.EmailAlreadyExistsException;
import by.dmitryskachkov.exception.exceptions.email.InvalidEmailFormatException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;
    public final JwtTokenHandler tokenHandler;

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtTokenHandler tokenHandler) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenHandler = tokenHandler;
    }

    @Override
    @Transactional
    public void createUser(UserDTO userDTO) {

        final String email = userDTO.getEmail();

        if (!EmailValidator.isValidEmail(email)) {
            throw new InvalidEmailFormatException("Invalid email format");
        }

        if (userRepo.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        try {
            userRepo.save(new UserEntity(
                    UUID.randomUUID(),
                    userDTO.getEmail(),
                    passwordEncoder.encode(userDTO.getPassword())));
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
    }

    @Override
    public String logIn(UserDTO userDTO) {

        UserEntity userEntity = userRepo.findByEmail(userDTO.getEmail())
                .orElseThrow(() ->  new ValidationException("Invalid email or password"));

        if (!BCrypt.checkpw(userDTO.getPassword(), userEntity.getPassword())) {
            throw new ValidationException("Invalid email or password");
        }

        String token = tokenHandler.generateAccessToken(new UserAuth(userEntity.getUuid().toString()));
        return token;
    }
}
