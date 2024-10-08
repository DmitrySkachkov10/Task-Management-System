package by.dmitry_skachkov.taskservice.core.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskCreateDto {

    private String header;

    private String description;

    private String status;

    private String priority;

    private Set<UUID> performersUuid = new HashSet<>();
}
