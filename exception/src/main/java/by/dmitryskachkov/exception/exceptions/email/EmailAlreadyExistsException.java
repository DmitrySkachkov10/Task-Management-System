package by.dmitryskachkov.exception.exceptions.email;

import by.dmitryskachkov.exception.exceptions.ValidationException;

public class EmailAlreadyExistsException extends ValidationException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
