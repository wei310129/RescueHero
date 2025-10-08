package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.RescueGroupTaskItemDto;
import tw.com.aidenmade.rescuehero.data.dto.RescueTeamMemberDto;
import tw.com.aidenmade.rescuehero.data.dto.RoleDto;

import java.util.UUID;

public interface RescueGroupTaskItemMemberRoleProjection {
    UUID getAuditId();
    RescueGroupTaskItemDto getTaskItem();
    RescueTeamMemberDto getMember();
    RoleDto getRole();
}
