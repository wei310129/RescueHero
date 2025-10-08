package tw.com.aidenmade.rescuehero.data.dto;

import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

/**
 * 人員
 */
public record PersonDto(
    // 主鍵ID
    Long id,
    // 審計資訊
    AuditInfoDto auditInfo,
    // 國籍
    CountryDto country,
    // 姓名
    String name,
    // 身分證號
    String identification,
    // 年齡
    Integer age,
    // 性別
    String gender,
    // 電話
    String phone,
    // 電子郵件
    String email,
    // 備註(病史)
    String note
) {}
