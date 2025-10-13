package tw.com.aidenmade.rescuehero.domain.common.projection;

import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

public interface StatusTypeProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    String getName();
    String getDescription();
}

