package tw.com.aidenmade.rescuehero.dto;

import lombok.*;
import java.time.LocalDate;

/**
 * 災害
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DisasterDto {
    // 主鍵 ID
    private Long id;
    // 稽核資訊ID
    private Long auditInfoId;
    // 災害狀態
    private String status;
    // 災害名稱
    private String name;
    // 發生日期 (只存到日 yyyy-MM-dd)
    private LocalDate occurredAt;
    // 發生地點
    private String location;
    // 災害描述
    private String description;
}
