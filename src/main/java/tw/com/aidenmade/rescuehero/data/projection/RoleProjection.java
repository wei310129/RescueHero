package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.data.dto.RoleTypeDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

public interface RoleProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterDto getDisaster();
    RoleTypeDto getRoleType();
    String getName();
    String getDescription();
}
