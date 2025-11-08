package tw.com.aidenmade.rescuehero.domain.rescue.api.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class RescueGroupTaskAcceptedRequest {
    Long groupId;
    Long disasterId;
    String nameLike;
    Integer priority;
    Pageable pageable;
}
