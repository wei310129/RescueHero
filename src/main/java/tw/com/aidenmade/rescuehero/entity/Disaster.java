package tw.com.aidenmade.rescuehero.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import tw.com.aidenmade.rescuehero.enums.DisasterStatus;

import java.time.LocalDate;

@Entity
@Table(name = "disaster")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Disaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("主鍵 ID")
    private Long id;

    @Column(name = "audit_id", nullable = false)
    @Comment("審計資訊 ID (audit_info 外鍵)")
    private String auditId;

    @Column(name = "status", nullable = false, length = 30)
    @Comment("災害狀態")
    private DisasterStatus status;

    @Column(nullable = false)
    @Comment("災害名稱")
    private String name;

    @Column(name = "occurred_at", nullable = false)
    @Comment("發生日期 (只存到日 yyyy-MM-dd)")
    private LocalDate occurredAt;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Comment("發生地點")
    private String location;

    @Column(columnDefinition = "TEXT")
    @Comment("災害描述")
    private String description;

}
