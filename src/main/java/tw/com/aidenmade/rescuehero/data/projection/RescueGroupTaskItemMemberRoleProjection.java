package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.RescueGroupTaskItemDto;
import tw.com.aidenmade.rescuehero.data.dto.RescueTeamMemberDto;
import tw.com.aidenmade.rescuehero.data.dto.RoleDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

public interface RescueGroupTaskItemMemberRoleProjection {
    AuditInfoDto getAuditInfo();
    RescueGroupTaskItemDto getTaskItem();
    RescueTeamMemberDto getMember();
    RoleDto getRole();
}
