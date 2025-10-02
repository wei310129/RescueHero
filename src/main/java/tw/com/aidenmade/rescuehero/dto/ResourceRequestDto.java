package tw.com.aidenmade.rescuehero.dto;

import lombok.*;

/**
 * 物資需求
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRequestDto {
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
    // 申請人員ID
    private Long requestedById;
    // 申請時間
    private java.time.Instant requestedAt;
    // 是否已滿足
    private Boolean fulfilled;
    // 滿足時間
    private java.time.Instant fulfilledAt;
    // 備註
    private String note;
}
