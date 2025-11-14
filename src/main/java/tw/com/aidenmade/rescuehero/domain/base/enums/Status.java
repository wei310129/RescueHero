package tw.com.aidenmade.rescuehero.domain.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    PENDING("待處理"),
    IN_PROGRESS("進行中"),
    COMPLETED("已完成"),
    CANCELLED("已取消"),
    ;
    private final String name;
}
