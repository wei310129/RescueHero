package tw.com.aidenmade.rescuehero.domain.disaster.application.dto;

import tw.com.aidenmade.rescuehero.domain.address.application.dto.AddressDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.CountryDto;
import tw.com.aidenmade.rescuehero.domain.disaster.enums.DisasterStatus;

import java.time.Instant;

/**
 * 災害
 */
public record DisasterDto(
    // 主鍵ID
    Long id,
    // 審計資訊
    AuditInfoDto auditInfo,
    // 國籍
    CountryDto country,
    // 災害狀態
    DisasterStatus status,
    // 災害名稱
    String name,
    // 發生日期 (只存到日 yyyy-MM-dd)
    Instant occurredAt,
    // 發生地點（Address 關聯）
    AddressDto location,
    // 災害描述
    String description
) {

    public DisasterDto(
            Long id,
//            AuditInfoDto auditInfo,
            CountryDto country,
            DisasterStatus status,
            String name,
            Instant occurredAt,
            AddressDto location,
            String description
    ) {
        this(id, null, country, status, name, occurredAt, location, description);
    }
}
