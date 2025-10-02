package tw.com.aidenmade.rescuehero.dto;

import lombok.*;

/**
 * 救援組織
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RescueOrganizationDto {
    // 主鍵
    private Long id;
    // 稽核資訊ID
    private Long auditInfoId;
    // 名稱
    private String name;
    // 描述
    private String description;
}

