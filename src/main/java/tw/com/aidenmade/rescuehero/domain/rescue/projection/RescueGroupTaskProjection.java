package tw.com.aidenmade.rescuehero.domain.rescue.projection;

import tw.com.aidenmade.rescuehero.domain.disaster.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.domain.rescue.dto.RescueGroupDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

public interface RescueGroupTaskProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    RescueGroupDto getGroup();
    DisasterDto getDisaster();
    String getName();
    String getDescription();
}
