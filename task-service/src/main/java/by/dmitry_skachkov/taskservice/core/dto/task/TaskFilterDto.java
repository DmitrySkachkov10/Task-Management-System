package by.dmitry_skachkov.taskservice.core.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskFilterDto {
    private int page = 1;
    private int size = 5;
    private UUID uuid;
    private boolean my = false;


}
