package tw.com.aidenmade.rescuehero.dto;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;
import tw.com.aidenmade.rescuehero.enums.DisasterStatus;

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
) {}
