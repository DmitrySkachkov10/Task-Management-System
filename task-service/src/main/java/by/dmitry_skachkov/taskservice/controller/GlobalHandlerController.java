package by.dmitry_skachkov.taskservice.controller;


import by.dmitryskachkov.exception.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandlerController {

    @ExceptionHandler(PerimissionDeniedException.class)

    public ResponseEntity<String> handlePerimissionDeniedException(PerimissionDeniedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUuidException.class)
    public ResponseEntity<String> handleInvalidUuidException(InvalidUuidException ex) {
        return new ResponseEntity<>("Invalid UUID: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<String> handleUnauthorizedActionException(UnauthorizedActionException ex) {
        return new ResponseEntity<>("Unauthorized action: " + ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(VersionConflictException.class)
    public ResponseEntity<String> handleVersionConflictException(VersionConflictException ex) {
        return new ResponseEntity<>("Version conflict: " + ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}