package tw.com.aidenmade.rescuehero.domain.rescue.projection;

import tw.com.aidenmade.rescuehero.domain.base.application.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.RoleDto;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueGroupTaskItemDto;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueTeamMemberDto;

public interface RescueGroupTaskItemMemberRoleProjection {
    AuditInfoDto getAuditInfo();
    RescueGroupTaskItemDto getTaskItem();
    RescueTeamMemberDto getMember();
    RoleDto getRole();
}
