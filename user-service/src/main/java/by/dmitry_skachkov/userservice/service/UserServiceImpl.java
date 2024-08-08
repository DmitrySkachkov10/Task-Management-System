package by.dmitry_skachkov.userservice.service;

import by.dmitry_skachkov.userservice.core.dto.UserDto;
import by.dmitry_skachkov.userservice.core.dto.UserLogin;
import by.dmitry_skachkov.userservice.core.dto.UserRegistration;
import by.dmitry_skachkov.userservice.core.utils.EmailValidator;
import by.dmitry_skachkov.userservice.core.utils.JwtTokenHandler;
import by.dmitry_skachkov.userservice.core.utils.UserAuth;
import by.dmitry_skachkov.userservice.repo.entity.UserEntity;
import by.dmitry_skachkov.userservice.repo.UserRepo;
import by.dmitry_skachkov.userservice.service.api.UserService;
import by.dmitryskachkov.exception.exceptions.ValidationException;
import by.dmitryskachkov.exception.exceptions.email.EmailAlreadyExistsException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public void createUser(UserRegistration userRegistration) {

        final String email = userRegistration.getEmail();
        final String password = userRegistration.getPassword();

        if (!EmailValidator.isValidEmail(email)) {
            throw new ValidationException("Invalid email format");
        }

        if (password.length() < 8 || password.length() > 32) {
            throw new ValidationException("Password must be between 8 and 32 characters");
        }

        if (userRepo.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        try {
            userRepo.save(new UserEntity(
                    UUID.randomUUID(),
                    userRegistration.getEmail(),
                    passwordEncoder.encode(userRegistration.getPassword())));
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
    }

    @Override
    public String logIn(UserLogin userLogin) {

        UserEntity userEntity = userRepo.findByEmail(userLogin.getEmail())
                .orElseThrow(() -> new ValidationException("Invalid email or password"));

        if (!BCrypt.checkpw(userEntity.getPassword(), userEntity.getPassword())) {
            throw new ValidationException("Invalid email or password");
        }

        String token = tokenHandler.generateAccessToken(new UserAuth(userEntity.getUuid().toString()));
        return token;
    }

    @Override
    public UserDto myInfo() {
        UserAuth auth = (UserAuth) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        UserEntity userEntity = userRepo.findById(UUID.fromString(auth.getUUID()))
                .orElseThrow(() -> new RuntimeException("Can`t get data"));

        return new UserDto(userEntity.getEmail());
    }
}
