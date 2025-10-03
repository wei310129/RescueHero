package tw.com.aidenmade.rescuehero.dto;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;

import java.math.BigDecimal;

/**
 * 單位
 */
public record UnitDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 名稱
    String name,
    // 地址
    String address,
    // 緯度
    BigDecimal latitude,
    // 經度
    BigDecimal longitude,
    // 聯絡人姓名
    String contactName,
    // 聯絡人電話
    String contactPhone
) {}
