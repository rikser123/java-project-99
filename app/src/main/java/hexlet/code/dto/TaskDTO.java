package hexlet.code.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private Integer index;
    private String title;
    private String content;
    //CHECKSTYLE.OFF: MemberName - Much more readable than catching 7 exceptions
    private Long assignee_id;
    //CHECKSTYLE.ON: MemberName
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
}
