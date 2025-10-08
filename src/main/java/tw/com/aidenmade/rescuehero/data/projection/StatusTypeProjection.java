package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

public interface StatusTypeProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    String getName();
    String getDescription();
}

