package tw.com.aidenmade.rescuehero.domain.common.projection;

import tw.com.aidenmade.rescuehero.domain.common.application.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.RoleTypeDto;
import tw.com.aidenmade.rescuehero.domain.disaster.application.dto.DisasterDto;

public interface RoleProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterDto getDisaster();
    RoleTypeDto getRoleType();
    String getName();
    String getDescription();
}
