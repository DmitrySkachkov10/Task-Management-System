package by.dmitry_skachkov.userservice.controller;


import by.dmitryskachkov.dto.ExcepionResponseDto;
import by.dmitryskachkov.exception.exceptions.ValidationException;
import by.dmitryskachkov.exception.exceptions.email.EmailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class GlobalHandlerController {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExcepionResponseDto> defaultExceptionHandler(ValidationException e) {
        HttpStatus status;

        if (e instanceof EmailAlreadyExistsException) {
            status = HttpStatus.CONFLICT;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(new ExcepionResponseDto(e.getMessage()), status);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExcepionResponseDto> defaultExceptionHandler(RuntimeException e) {
        ExcepionResponseDto responseDto = new ExcepionResponseDto("An unexpected error occurred");
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
