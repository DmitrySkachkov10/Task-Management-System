package by.dmitry_skachkov.taskservice.model;

public enum Priority {
    HIGH,
    MEDIUM,
    LOW;

    public static Priority fromString(String value) {
        try {
            return Priority.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Priority: " + value);
        }
    }
}
