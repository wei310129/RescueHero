package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;
import tw.com.aidenmade.rescuehero.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.dto.RoleTypeDto;

public interface RoleProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterDto getDisaster();
    RoleTypeDto getRoleType();
    String getName();
    String getDescription();
}
