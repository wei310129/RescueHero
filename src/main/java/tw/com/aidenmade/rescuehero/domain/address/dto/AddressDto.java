package tw.com.aidenmade.rescuehero.domain.address.dto;

import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.CountryDto;

/**
 * 地址
 */
public record AddressDto(
    // 主鍵ID
    Long id,
    // 審計資訊
    AuditInfoDto auditInfo,
    // 所屬國家
    CountryDto country,
    // 所屬地址細胞（最末層節點）
    AddressCellDto addressCell,
    // 詳細資訊 (如 "123號3樓5室")
    String detail,
    // 完整地址
    String fullAddress,
    // 緯度
    Double latitude,
    // 經度
    Double longitude
) {}
