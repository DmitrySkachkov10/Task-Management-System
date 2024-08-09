package by.dmitryskachkov.exception.exceptions;

public class VersionConflictException extends RuntimeException{
    public VersionConflictException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
