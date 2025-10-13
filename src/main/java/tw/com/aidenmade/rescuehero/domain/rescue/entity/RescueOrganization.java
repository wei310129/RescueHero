package tw.com.aidenmade.rescuehero.domain.rescue.entity;

import jakarta.persistence.*;
import lombok.*;
import tw.com.aidenmade.rescuehero.domain.common.entity.AuditInfo;
import tw.com.aidenmade.rescuehero.domain.disaster.entity.Disaster;
import tw.com.aidenmade.rescuehero.domain.common.entity.Unit;


/**
 * 救援組織
 */
@Entity
@Table(name = "rescue_organization",
        indexes = @Index(name = "idx_rescue_org_audit_id", columnList = "audit_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RescueOrganization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditInfo auditInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disaster_id", nullable = false)
    private Disaster disaster;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false, unique = true)
    private Unit unit;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
