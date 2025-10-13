package tw.com.aidenmade.rescuehero.domain.common.application.dto;

import tw.com.aidenmade.rescuehero.domain.disaster.application.dto.DisasterDto;

/**
 * 角色
 */
public record RoleDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 災害ID
    DisasterDto disaster,
    // 角色類型ID
    RoleTypeDto roleType,
    // 名稱
    String name,
    // 描述
    String description
) {}
