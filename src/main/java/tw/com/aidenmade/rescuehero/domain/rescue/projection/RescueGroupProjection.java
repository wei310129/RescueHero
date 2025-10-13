package tw.com.aidenmade.rescuehero.domain.rescue.projection;

import tw.com.aidenmade.rescuehero.domain.disaster.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.domain.rescue.dto.RescueOrganizationDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

public interface RescueGroupProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterDto getDisaster();
    RescueOrganizationDto getOrganization();
    String getName();
    String getDescription();
}
