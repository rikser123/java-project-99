package hexlet.code.mapper;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        uses = { JsonNullMapper.class},
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Mapping(source = "name", target = "title")
    @Mapping(source = "description", target = "content")
    @Mapping(source = "taskStatus.slug", target = "status")
    @Mapping(source = "assignee.id", target = "assignee_id")
    @Mapping(source = "taskLabels", target = "taskLabelIds", qualifiedByName = "labelIds")
    public abstract TaskDTO map(Task model);

    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "assignee_id", target = "assignee", qualifiedByName = "assigneeModel")
    @Mapping(source = "status", target = "taskStatus", qualifiedByName = "taskStatusModel")
    @Mapping(source = "taskLabelIds", target = "taskLabels", qualifiedByName = "labels")
    public abstract Task map(TaskCreateDTO dto);


    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "assignee_id", target = "assignee", qualifiedByName = "assigneeModel")
    @Mapping(source = "status", target = "taskStatus", qualifiedByName = "taskStatusModel")
    @Mapping(source = "taskLabelIds", target = "taskLabels", qualifiedByName = "labels")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

    @Named("assigneeModel")
    public User findUser(Long id) {
        if (id == null) {
            return null;
        }
        return userRepository.findById(id).orElse(null);
    }

    @Named("taskStatusModel")
    public TaskStatus getTaskStatus(String slug) {
        return taskStatusRepository.findBySlug(slug).get();
    }

    @Named("labelIds")
    public List<Long> getLabelIds(List<Label> labels) {
        return labels.stream().map(Label::getId).toList();
    }

    @Named("labels")
    public List<Label> getLabels(List<Long> ids) {
        return labelRepository.findAllById(ids);
    }
}
