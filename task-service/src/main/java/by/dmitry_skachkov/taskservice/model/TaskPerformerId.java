package by.dmitry_skachkov.taskservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskPerformerId implements Serializable {

    private UUID taskUuid;
    private UUID performerUuid;

}
