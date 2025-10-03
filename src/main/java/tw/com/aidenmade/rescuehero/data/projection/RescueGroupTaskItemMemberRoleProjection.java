package tw.com.aidenmade.rescuehero.data.projection;

import java.util.UUID;
import tw.com.aidenmade.rescuehero.dto.RescueGroupTaskItemDto;
import tw.com.aidenmade.rescuehero.dto.RescueTeamMemberDto;
import tw.com.aidenmade.rescuehero.dto.RoleDto;

public interface RescueGroupTaskItemMemberRoleProjection {
    UUID getAuditId();
    RescueGroupTaskItemDto getTaskItem();
    RescueTeamMemberDto getMember();
    RoleDto getRole();
}
