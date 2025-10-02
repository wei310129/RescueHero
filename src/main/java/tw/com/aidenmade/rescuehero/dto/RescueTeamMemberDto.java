package tw.com.aidenmade.rescuehero.dto;

import lombok.*;

/**
 * 救援團隊成員
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RescueTeamMemberDto {
    // 主鍵
    private Long id;
    // 稽核資訊ID
    private Long auditInfoId;
    // 人員ID
    private Long personId;
    // 團隊ID
    private Long teamId;
    // 組織ID
    private Long organizationId;
}

