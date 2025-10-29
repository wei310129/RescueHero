package tw.com.aidenmade.rescuehero.domain.address.application.dto;

import tw.com.aidenmade.rescuehero.domain.base.application.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.CountryDto;

/**
 * 地址單位層級
 */
public record AddressLevelDto(
    // 主鍵ID
    Long id,
    // 審計資訊
    AuditInfoDto auditInfo,
    // 所屬國家
    CountryDto country,
    // 層級名稱 (ex: 縣市、區、鄉、路、街)
    String name,
    // 後綴字（如: 一段、二段）
    String suffix,
    // 層級順序
    Integer sequence
) {}
