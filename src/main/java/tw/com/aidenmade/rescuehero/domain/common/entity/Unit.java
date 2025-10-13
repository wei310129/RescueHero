package tw.com.aidenmade.rescuehero.domain.common.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import tw.com.aidenmade.rescuehero.domain.address.entity.Address;

/**
 * 單位表（受災戶、救援隊等）
 */
@Entity
@Table(name = "unit",
        indexes = @Index(name = "idx_unit_audit_id", columnList = "audit_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditInfo auditInfo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    @Comment("國籍")
    private Country country;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location", nullable = false)
    @Comment("地址 (家庭住址或營地)")
    private Address location;

    @Column(name = "contact_name", length = 100)
    @Comment("聯絡人姓名")
    private String contactName;

    @Column(name = "contact_phone", length = 50)
    @Comment("聯絡人電話")
    private String contactPhone;
}