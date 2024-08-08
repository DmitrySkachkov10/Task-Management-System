package by.dmitry_skachkov.taskservice.core.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageOfTask {

    private List<TaskDto> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
