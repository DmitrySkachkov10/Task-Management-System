package by.dmitryskachkov.exception.exceptions;

public class InvalidUuidException extends RuntimeException {
    public InvalidUuidException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
