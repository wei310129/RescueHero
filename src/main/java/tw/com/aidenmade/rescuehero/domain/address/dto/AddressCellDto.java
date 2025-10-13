package tw.com.aidenmade.rescuehero.domain.address.dto;

import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

/**
 * 地址細胞
 */
public record AddressCellDto(
    // 主鍵ID
    Long id,
    // 審計資訊
    AuditInfoDto auditInfo,
    // 所屬層級（如縣市、區、鄉等）
    AddressLevelDto level,
    // 上層細胞（如中正區→台北市）
    AddressCellDto parent,
    // 地點名稱
    String name
) {}
