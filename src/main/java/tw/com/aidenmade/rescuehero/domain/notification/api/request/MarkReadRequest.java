package tw.com.aidenmade.rescuehero.domain.notification.api.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MarkReadRequest {
    List<String> notificationIds;
}
