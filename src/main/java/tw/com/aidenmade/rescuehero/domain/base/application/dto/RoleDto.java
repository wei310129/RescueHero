package tw.com.aidenmade.rescuehero.domain.base.application.dto;

/**
 * 角色
 */
public record RoleDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 角色類型ID
    RoleTypeDto roleType,
    // 名稱
    String name,
    // 描述
    String description
) {
    public String getUniquePattern() {
        return roleType.name() + "_" + name;
    }
}
