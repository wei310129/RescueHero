package tw.com.aidenmade.rescuehero.domain.rescue.api.request;

import lombok.Getter;
import lombok.Setter;
import tw.com.aidenmade.rescuehero.domain.base.api.request.PageableRequest;

@Getter
@Setter
public class RescueGroupTaskAcceptedRequest {
    private Long groupId;
    private Long disasterId;
    private String nameLike;
    private Integer priority;
    private PageableRequest page;
}
