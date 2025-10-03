package tw.com.aidenmade.rescuehero.dto;

import lombok.*;

import java.util.UUID;

/**
 * 任務工項成員角色
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RescueGroupTaskItemMemberRoleDto {
    // 稽核資訊ID
    private UUID auditId;
    // 工項
    private RescueGroupTaskItemDto taskItem;
    // 成員
    private RescueTeamMemberDto member;
    // 角色
    private RoleDto role;
}

