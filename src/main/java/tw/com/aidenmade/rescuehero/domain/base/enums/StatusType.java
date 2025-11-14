package tw.com.aidenmade.rescuehero.domain.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusType {
    TASK("任務"),
    ;
    private final String name;
}
