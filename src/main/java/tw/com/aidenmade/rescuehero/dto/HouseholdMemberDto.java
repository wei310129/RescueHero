package tw.com.aidenmade.rescuehero.dto;

import lombok.*;

/**
 * 受災戶成員
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HouseholdMemberDto {
    // 主鍵
    private Long id;
    // 稽核資訊ID
    private Long auditInfoId;
    // 人員ID
    private Long personId;
    // 受災戶ID
    private Long householdId;
    // 年齡
    private Integer age;
    // 性別
    private String gender;
    // 狀態ID
    private Long statusId;
}
