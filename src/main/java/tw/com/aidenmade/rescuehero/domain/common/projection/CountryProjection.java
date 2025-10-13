package tw.com.aidenmade.rescuehero.domain.common.projection;

import tw.com.aidenmade.rescuehero.domain.common.application.dto.AuditInfoDto;

public interface CountryProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    String getName();
    String getNativeName();
    String getCode();
}

