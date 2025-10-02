package tw.com.aidenmade.rescuehero.dto;

import lombok.*;

/**
 * 物資
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDto {
    // 主鍵
    private Long id;
    // 稽核資訊ID
    private Long auditInfoId;
    // 物資類型ID
    private Long resourceTypeId;
    // 名稱
    private String name;
    // 單位
    private String unit;
    // 描述
    private String description;
}

