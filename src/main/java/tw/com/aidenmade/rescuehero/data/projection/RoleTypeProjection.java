package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;

public interface RoleTypeProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    String getName();
    String getDescription();
}

