package tw.com.aidenmade.rescuehero.domain.common.dto;

/**
 * 角色類型
 */
public record RoleTypeDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 名稱
    String name,
    // 描述
    String description
) {}
