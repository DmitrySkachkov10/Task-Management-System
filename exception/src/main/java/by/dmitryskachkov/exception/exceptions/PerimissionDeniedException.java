package by.dmitryskachkov.exception.exceptions;

public class PerimissionDeniedException extends RuntimeException{
    public PerimissionDeniedException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
