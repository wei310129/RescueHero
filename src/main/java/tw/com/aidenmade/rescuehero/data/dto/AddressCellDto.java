package tw.com.aidenmade.rescuehero.data.dto;

import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

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
    // 地點名稱
    String name
) {}
