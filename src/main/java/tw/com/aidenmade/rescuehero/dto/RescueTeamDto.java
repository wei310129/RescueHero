package tw.com.aidenmade.rescuehero.dto;

import lombok.*;

/**
 * 救援團隊
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RescueTeamDto {
    // 主鍵
    private Long id;
    // 稽核資訊ID
    private Long auditInfoId;
    // 單元ID
    private Long unitId;
    // 群組ID
    private Long groupId;
    // 狀態ID
    private Long statusId;
    // 聯絡Email
    private String contactEmail;
}

