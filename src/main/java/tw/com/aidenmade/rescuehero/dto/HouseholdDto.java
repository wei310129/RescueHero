package tw.com.aidenmade.rescuehero.dto;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;

/**
 * 受災戶
 */
public record HouseholdDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 單元ID
    UnitDto unit,
    // 災害ID
    DisasterDto disasterId,
    // 狀態ID
    StatusDto status,
    // 備註
    String note
) {}
