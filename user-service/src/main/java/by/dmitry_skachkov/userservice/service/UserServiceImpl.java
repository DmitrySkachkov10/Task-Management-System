package by.dmitry_skachkov.userservice.service;

import by.dmitry_skachkov.userservice.core.dto.UserDTO;
import by.dmitry_skachkov.userservice.core.utils.EmailValidator;
import by.dmitry_skachkov.userservice.core.utils.JwtTokenHandler;
import by.dmitry_skachkov.userservice.core.utils.UserAuth;
import by.dmitry_skachkov.userservice.entity.UserEntity;
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

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
        logger.info("Creating user with email: {}", email);

        if (!EmailValidator.isValidEmail(email)) {
            logger.error("Invalid email format: {}", email);
            throw new InvalidEmailFormatException("Invalid email format");
        }

        if (userRepo.existsByEmail(email)) {
            logger.error("Email already exists: {}", email);
            throw new EmailAlreadyExistsException("Email already exists");
        }
        try {
            userRepo.save(new UserEntity(
                    UUID.randomUUID(),
                    userDTO.getEmail(),
                    passwordEncoder.encode(userDTO.getPassword())));
            logger.info("User created successfully with email: {}", email);
        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation when creating user with email: {}", email, e);
            throw new EmailAlreadyExistsException("Email already exists");
        }
    }

    @Override
    public String logIn(UserDTO userDTO) {
        logger.info("Logging in user with email: {}", userDTO.getEmail());

        UserEntity userEntity = userRepo.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> {
                    logger.error("Invalid email or password for email: {}", userDTO.getEmail());
                    return new ValidationException("Invalid email or password");
                });

        if (!BCrypt.checkpw(userDTO.getPassword(), userEntity.getPassword())) {
            logger.error("Invalid password for email: {}", userDTO.getEmail());
            throw new ValidationException("Invalid input data");
        }

        String token = tokenHandler.generateAccessToken(new UserAuth(userEntity.getUuid().toString()));
        logger.info("User logged in successfully with email: {}", userDTO.getEmail());
        return token;
    }
}
