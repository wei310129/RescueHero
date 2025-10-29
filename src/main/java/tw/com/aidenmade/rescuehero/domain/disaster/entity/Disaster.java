package tw.com.aidenmade.rescuehero.domain.disaster.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import tw.com.aidenmade.rescuehero.domain.address.entity.Address;
import tw.com.aidenmade.rescuehero.domain.base.entity.AuditInfo;
import tw.com.aidenmade.rescuehero.domain.base.entity.Country;
import tw.com.aidenmade.rescuehero.domain.disaster.enums.DisasterStatus;

import java.time.Instant;

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

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditInfo auditInfo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    @Comment("國籍")
    private Country country;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Comment("災害狀態")
    private DisasterStatus status;

    @Column(nullable = false)
    @Comment("災害名稱")
    private String name;

    @Column(name = "occurred_at", nullable = false)
    @Comment("發生日期 (只存到日 yyyy-MM-dd)")
    private Instant occurredAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location", nullable = false)
    @Comment("發生地點（Address 關聯）")
    private Address location;

    @Column(columnDefinition = "TEXT")
    @Comment("災害描述")
    private String description;

}
