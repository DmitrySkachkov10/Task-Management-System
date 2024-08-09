package by.dmitry_skachkov.taskservice.core.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskDto {

    private boolean author;

    private boolean performer;

    private String uuid;

    private String header;

    private String description;

    private String status;

    private String priority;

    private String authorUuid;

    private long version;

    private Set<String> comments = new HashSet<>();

    private Set<UUID> performersUuid = new HashSet<>();

}
