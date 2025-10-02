package tw.com.aidenmade.rescuehero.dto;

import lombok.*;

/**
 * 狀態
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StatusDto {
    // 主鍵 ID
    private Long id;
    // 稽核資訊ID
    private Long auditInfoId;
    // 所屬災害ID
    private Long disasterId;
    // 狀態類型ID
    private Long statusTypeId;
    // 狀態代碼 (ex: pending, completed)
    private String code;
    // 顯示名稱 (例: 待處理, 已完成)
    private String name;
    // 狀態說明
    private String description;
}
