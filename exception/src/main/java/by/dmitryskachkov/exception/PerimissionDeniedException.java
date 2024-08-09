package by.dmitryskachkov.exception;

public class PerimissionDeniedException extends RuntimeException{
    public PerimissionDeniedException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
