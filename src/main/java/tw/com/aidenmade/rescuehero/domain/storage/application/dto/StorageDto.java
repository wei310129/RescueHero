package tw.com.aidenmade.rescuehero.domain.storage.application.dto;

import tw.com.aidenmade.rescuehero.domain.address.application.dto.AddressDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.CountryDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.StatusDto;

/**
 * 災害物資庫存站
 */
public record StorageDto(
    // 主鍵ID
    Long id,
    // 審計資訊
    AuditInfoDto auditInfo,
    // 國籍
    CountryDto country,
    // 庫存類型
    StorageTypeDto storageType,
    // 狀態
    StatusDto status,
    // 名稱
    String name,
    // 地址
    AddressDto address,
    // 聯絡人姓名
    String contactName,
    // 聯絡人電話
    String contactPhone,
    // 備註
    String note,
    // 容量
    Integer capacity
) {}
