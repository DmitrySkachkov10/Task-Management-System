package by.dmitry_skachkov.taskservice.model;

public enum Status {
    IN_PROGRESS,
    FINISHED,
    CANCELED,
    PENDING;

    public static Status fromString(String value) {
        try {
            return Status.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Status: " + value);
        }
    }
}
