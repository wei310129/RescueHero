package tw.com.aidenmade.rescuehero.domain.common.projection;

import tw.com.aidenmade.rescuehero.domain.common.application.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.RoleTypeDto;

public interface RoleProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    RoleTypeDto getRoleType();
    String getName();
    String getDescription();
}
