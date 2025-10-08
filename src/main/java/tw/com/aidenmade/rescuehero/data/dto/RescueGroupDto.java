package tw.com.aidenmade.rescuehero.data.dto;

import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

/**
 * 救援群組
 */
public record RescueGroupDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 災害ID
    DisasterDto disaster,
    // 組織ID
    RescueOrganizationDto organization,
    // 名稱
    String name,
    // 描述
    String description
) {}
