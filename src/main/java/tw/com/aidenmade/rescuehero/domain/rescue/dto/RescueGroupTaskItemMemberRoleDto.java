package tw.com.aidenmade.rescuehero.domain.rescue.dto;

import lombok.*;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.RoleDto;

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
    private AuditInfoDto auditInfo;
    // 工項
    private RescueGroupTaskItemDto taskItem;
    // 成員
    private RescueTeamMemberDto member;
    // 角色
    private RoleDto role;
}

