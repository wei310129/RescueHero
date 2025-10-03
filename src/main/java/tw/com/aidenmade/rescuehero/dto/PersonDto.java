package tw.com.aidenmade.rescuehero.dto;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;

/**
 * 人員表
 */
public record PersonDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
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
    // 備註
    String note
) {}
