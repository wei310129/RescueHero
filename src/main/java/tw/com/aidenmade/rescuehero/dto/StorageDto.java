package tw.com.aidenmade.rescuehero.dto;

import lombok.*;
import java.math.BigDecimal;

/**
 * 物資儲存站
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StorageDto {
    // 主鍵
    private Long id;
    // 稽核資訊ID
    private Long auditInfoId;
    // 儲存站類型ID
    private Long storageTypeId;
    // 狀態ID
    private Long statusId;
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
    // 容量
    private Integer capacity;
}
