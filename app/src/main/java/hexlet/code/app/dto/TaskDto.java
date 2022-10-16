package hexlet.code.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private static final int MIN_V = 1;

    @NotBlank
    @Size(min = MIN_V)
    private String name;

    private String description;

    private long executorId;

    @NotNull
    private long taskStatusId;

    private Set<Long> labelIds;
}
