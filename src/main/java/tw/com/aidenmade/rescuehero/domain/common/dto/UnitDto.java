package tw.com.aidenmade.rescuehero.domain.common.dto;

import tw.com.aidenmade.rescuehero.domain.address.dto.AddressDto;

/**
 * 單位
 */
public record UnitDto(
    // 主鍵ID
    Long id,
    // 審計資訊
    AuditInfoDto auditInfo,
    // 國籍
    CountryDto country,
    // 名稱
    String name,
    // 地址
    AddressDto address,
    // 聯絡人姓名
    String contactName,
    // 聯絡人電話
    String contactPhone
) {}
