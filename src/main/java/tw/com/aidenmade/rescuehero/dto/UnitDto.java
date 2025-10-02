package tw.com.aidenmade.rescuehero.dto;

import lombok.*;
import java.math.BigDecimal;

/**
 * 單位
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UnitDto {
    // 主鍵
    private Long id;
    // 稽核資訊ID
    private Long auditInfoId;
    // 名稱
    private String name;
    // 地址
    private String address;
    // 緯度
    private BigDecimal latitude;
    // 經度
    private BigDecimal longitude;
    // 聯絡人姓名
    private String contactName;
    // 聯絡人電話
    private String contactPhone;
}
