package tw.com.aidenmade.rescuehero.domain.rescue.api.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class RescueGroupTaskAvailableRequest {
    Long groupId;
    Long disasterId;
    String nameLike;
    Long statusId;
    Integer priority;
    Pageable pageable;
}
