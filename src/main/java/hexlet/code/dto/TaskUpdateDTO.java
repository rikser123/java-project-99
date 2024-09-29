package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;

@Getter
@Setter
public class TaskUpdateDTO {
    @NotBlank
    private JsonNullable<String> title;
    private JsonNullable<String> content;
    private JsonNullable<Integer> index;
    private JsonNullable<List<Long>> taskLabelIds;
    @NotBlank
    private JsonNullable<String> status;
    //CHECKSTYLE.OFF: MemberName - Much more readable than catching 7 exceptions
    private JsonNullable<Long> assignee_id;
    //CHECKSTYLE.ON: MemberName
}
