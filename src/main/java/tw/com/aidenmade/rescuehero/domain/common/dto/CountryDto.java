package tw.com.aidenmade.rescuehero.domain.common.dto;

/**
 * 國家
 */
public record CountryDto(
    // 主鍵ID
    Long id,
    // 審計資訊
    AuditInfoDto auditInfo,
    // 國家名稱
    String name,
    // 本地語言名稱
    String nativeName,
    // 國家代碼 (如 TW, US)
    String code
) {}
