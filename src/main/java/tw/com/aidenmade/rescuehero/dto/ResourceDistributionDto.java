package tw.com.aidenmade.rescuehero.dto;

import lombok.*;
import java.time.Instant;

/**
 * 物資分配紀錄
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDistributionDto {
    // 主鍵
    private Long id;
    // 稽核資訊ID
    private Long auditInfoId;
    // 災害ID
    private Long disasterId;
    // 物資ID
    private Long resourceId;
    // 數量
    private Integer quantity;
    // 配送人員ID
    private Long deliveredById;
    // 受領單位ID
    private Long recipientUnitId;
    // 受領人員ID
    private Long recipientPersonId;
    // 配送時間
    private Instant deliveredAt;
    // 備註
    private String note;
}

