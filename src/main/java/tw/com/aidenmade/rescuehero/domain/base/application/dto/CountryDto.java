package tw.com.aidenmade.rescuehero.domain.base.application.dto;

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
) {
    public CountryDto(
            Long id,
//            AuditInfoDto auditInfo,
            String name,
            String nativeName,
            String code
    ) {
        this(id, null, name, nativeName, code);
    }
}
