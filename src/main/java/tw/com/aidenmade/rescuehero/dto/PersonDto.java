package tw.com.aidenmade.rescuehero.dto;

import lombok.*;

/**
 * 人員表
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
    // 主鍵
    private Long id;
    // 稽核資訊ID
    private Long auditInfoId;
    // 姓名
    private String name;
    // 身分證號
    private String identification;
    // 電話
    private String phone;
    // 電子郵件
    private String email;
}
