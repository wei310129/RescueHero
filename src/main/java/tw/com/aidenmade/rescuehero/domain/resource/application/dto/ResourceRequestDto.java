package tw.com.aidenmade.rescuehero.domain.resource.application.dto;

import tw.com.aidenmade.rescuehero.domain.base.application.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.PersonDto;
import tw.com.aidenmade.rescuehero.domain.disaster.application.dto.DisasterDto;

import java.time.ZonedDateTime;

/**
 * 物資需求
 */
public record ResourceRequestDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 災害ID
    DisasterDto disaster,
    // 物資ID
    ResourceDto resource,
    // 數量
    Integer quantity,
    // 申請人員ID
    PersonDto requestedBy,
    // 申請時間
    ZonedDateTime requestedAt,
    // 是否已滿足
    Boolean fulfilled,
    // 滿足時間
    ZonedDateTime fulfilledAt,
    // 備註
    String note
) {}
