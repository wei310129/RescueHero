package tw.com.aidenmade.rescuehero.data.projection;

import java.math.BigDecimal;
import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;

public interface UnitProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    String getName();
    String getAddress();
    BigDecimal getLatitude();
    BigDecimal getLongitude();
    String getContactName();
    String getContactPhone();
}
