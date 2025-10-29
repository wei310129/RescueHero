package tw.com.aidenmade.rescuehero.domain.account.application.dto;

import tw.com.aidenmade.rescuehero.domain.base.application.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.RoleDto;

/**
 * 帳號
 */
public record AccountDto(
    // 主鍵ID
    Long id,
    // 審計資訊
    AuditInfoDto auditInfo,
    // 角色ID
    RoleDto role,
    // 帳號
    String username,
    // 密碼
    String passwordHash,
    // 電子郵件
    String email,
    // 手機號碼
    String phone,
    // Google OAuth2 ID
    String googleId,
    // 暱稱
    String nickname,
    // 啟用狀態
    Boolean isActive,
    // 是否為管理員
    Boolean isAdmin
) {}
