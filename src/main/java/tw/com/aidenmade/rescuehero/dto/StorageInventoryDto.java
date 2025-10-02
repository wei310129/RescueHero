package tw.com.aidenmade.rescuehero.dto;

import lombok.*;

/**
 * 儲存站物資庫存
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StorageInventoryDto {
    // 主鍵
    private Long id;
    // 稽核資訊ID
    private Long auditInfoId;
    // 災害ID
    private Long disasterId;
    // 儲存站ID
    private Long storageId;
    // 物資ID
    private Long resourceId;
    // 數量
    private Integer quantity;
}
