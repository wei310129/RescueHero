package tw.com.aidenmade.rescuehero.data.dto;

import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

/**
 * 救援組織
 */
public record RescueOrganizationDto(
    // 主鍵ID
    Long id,
    // 審計資訊
    AuditInfoDto auditInfo,
    // 所屬災害
    DisasterDto disaster,
    // 所屬單位
    UnitDto unit,
    // 組織描述
    String description
) {}
