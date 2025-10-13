package tw.com.aidenmade.rescuehero.domain.rescue.projection;

import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.RoleDto;
import tw.com.aidenmade.rescuehero.domain.rescue.dto.RescueGroupTaskItemDto;
import tw.com.aidenmade.rescuehero.domain.rescue.dto.RescueTeamMemberDto;

public interface RescueGroupTaskItemMemberRoleProjection {
    AuditInfoDto getAuditInfo();
    RescueGroupTaskItemDto getTaskItem();
    RescueTeamMemberDto getMember();
    RoleDto getRole();
}
