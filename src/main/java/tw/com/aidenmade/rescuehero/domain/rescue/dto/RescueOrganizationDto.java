package tw.com.aidenmade.rescuehero.domain.rescue.dto;

import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.disaster.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.UnitDto;

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
