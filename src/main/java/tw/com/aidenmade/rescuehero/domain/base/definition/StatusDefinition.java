package tw.com.aidenmade.rescuehero.domain.base.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tw.com.aidenmade.rescuehero.domain.base.enums.Status;
import tw.com.aidenmade.rescuehero.domain.base.enums.StatusType;

@Getter
@AllArgsConstructor
public enum StatusDefinition {
    TASK_IN_PENDING(StatusType.TASK, Status.PENDING),
    TASK_IN_PROGRESS(StatusType.TASK, Status.IN_PROGRESS),
    TASK_COMPLETED(StatusType.TASK, Status.COMPLETED),
    TASK_CANCELLED(StatusType.TASK, Status.CANCELLED),
    ;

    private final StatusType type;
    private final Status status;
    private final String name;

    StatusDefinition(StatusType type, Status status) {
        this(type, status, type.getName() + status.getName());
    }

    public String getTypeName() {
        return this.type.name();
    }

    public String getStatusName() {
        return this.status.name();
    }
}
