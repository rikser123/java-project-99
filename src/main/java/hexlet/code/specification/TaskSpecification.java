package hexlet.code.specification;

import hexlet.code.dto.TaskFIlterDTO;
import hexlet.code.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {
    public Specification<Task> build(TaskFIlterDTO filter) {
        return withTitleCont(filter.getTitleCont())
                .and(withAssigneeId(filter.getAssigneeId()))
                .and(withStatus(filter.getStatus()))
                .and(withLabelId(filter.getLabelId()));
    }

    private Specification<Task> withTitleCont(String title) {
        return (root, query, cb) -> title == null
                ? cb.conjunction()
                : cb.like(root.get("name"), "%" + title + "%");
    }

    private Specification<Task> withAssigneeId(Long assigneeId) {
        return (root, query, cb) -> assigneeId == null
                ? cb.conjunction()
                : cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    private Specification<Task> withStatus(String status) {
        return (root, query, cb) -> status == null
                ? cb.conjunction()
                : cb.equal(root.get("taskStatus").get("slug"), status);
    }

    private Specification<Task> withLabelId(Long labelId) {
        return (root, query, cb) -> labelId == null
                ? cb.conjunction()
                : cb.equal(root.get("taskLabels").get("id"), labelId);
    }
}
