package tw.com.aidenmade.rescuehero.dto;

import lombok.*;

/**
 * 受災戶
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HouseholdDto {
    // 主鍵
    private Long id;
    // 稽核資訊ID
    private Long auditInfoId;
    // 單元ID
    private Long unitId;
    // 災害ID
    private Long disasterId;
    // 狀態ID
    private Long statusId;
    // 備註
    private String note;
}
