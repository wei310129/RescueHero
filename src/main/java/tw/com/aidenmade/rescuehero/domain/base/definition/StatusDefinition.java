package tw.com.aidenmade.rescuehero.domain.base.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tw.com.aidenmade.rescuehero.domain.base.enums.Status;
import tw.com.aidenmade.rescuehero.domain.base.enums.StatusType;

@Getter
@AllArgsConstructor
public enum StatusDefinition {
    TASK_ACCEPTED(StatusType.TASK, Status.ACCEPTED),
    ;

    private final StatusType type;
    private final Status status;

    public String getTypeName() {
        return this.type.name();
    }

    public String getStatusName() {
        return this.status.name();
    }
}
