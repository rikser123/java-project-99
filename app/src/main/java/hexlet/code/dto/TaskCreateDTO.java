package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskCreateDTO {
    private Integer index;

    //CHECKSTYLE.OFF: MemberName - Much more readable than catching 7 exceptions
    private Long assignee_id;
    //CHECKSTYLE.ON: MemberName

    private List<Long> taskLabelIds;

    @NotBlank
    private String title;

    private String content;

    @NotBlank
    private String status;
}
