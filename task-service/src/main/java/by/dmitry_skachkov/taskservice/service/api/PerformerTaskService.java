package by.dmitry_skachkov.taskservice.service.api;

import java.util.UUID;

public interface PerformerTaskService {

    void changeStatus(String status, long version, UUID uuid);
}
