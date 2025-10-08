package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.data.dto.RescueOrganizationDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

public interface RescueGroupProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterDto getDisaster();
    RescueOrganizationDto getOrganization();
    String getName();
    String getDescription();
}
