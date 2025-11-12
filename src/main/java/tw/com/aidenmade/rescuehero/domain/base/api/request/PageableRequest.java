package tw.com.aidenmade.rescuehero.domain.base.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageableRequest {
    private Integer page;
    private Integer size;
}
