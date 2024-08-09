package by.dmitry_skachkov.taskservice.core.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCommentDto {

    private String text;

    private UUID uuid;
}
