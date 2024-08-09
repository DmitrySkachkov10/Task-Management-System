package by.dmitry_skachkov.taskservice.model;

import by.dmitryskachkov.exception.exceptions.ValidationException;

public enum Priority {
    HIGH,
    MEDIUM,
    LOW;

    public static Priority fromString(String value) {
        try {
            return Priority.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid Priority: " + value);
        }
    }
}
