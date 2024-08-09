package by.dmitry_skachkov.taskservice.model;

import by.dmitryskachkov.exception.exceptions.ValidationException;

public enum Status {
    IN_PROGRESS,
    FINISHED,
    CANCELED,
    PENDING;

    public static Status fromString(String value) {
        try {
            return Status.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid Status: " + value);
        }
    }
}
