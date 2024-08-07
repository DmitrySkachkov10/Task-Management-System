package by.dmitryskachkov.exception.exceptions.email;

import by.dmitryskachkov.exception.exceptions.ValidationException;

public class InvalidEmailFormatException extends ValidationException {
    public InvalidEmailFormatException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
