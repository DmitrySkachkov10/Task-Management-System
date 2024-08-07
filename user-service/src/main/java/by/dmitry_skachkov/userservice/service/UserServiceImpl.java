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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    public final JwtTokenHandler tokenHandler;

    public UserServiceImpl(UserRepo userRepo, JwtTokenHandler tokenHandler) {
        this.userRepo = userRepo;
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
                    BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt())));
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistsException("Email already exists"); //todo log другой
        }
    }

    @Override
    public String logIn(UserDTO userDTO) {
        UserEntity userEntity = userRepo.findByEmail(userDTO.getEmail());

        if (!BCrypt.checkpw(userDTO.getPassword(), userEntity.getPassword())) {
            throw new ValidationException("Invalid input data");
        }
        return tokenHandler.generateAccessToken(new UserAuth(userEntity.getUuid().toString()));
    }
}
