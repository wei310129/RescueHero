package tw.com.aidenmade.rescuehero.domain.common.projection;

import tw.com.aidenmade.rescuehero.domain.disaster.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.StatusTypeDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

public interface StatusProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterDto getDisaster();
    StatusTypeDto getType();
    String getCode();
    String getName();
    String getDescription();
}
