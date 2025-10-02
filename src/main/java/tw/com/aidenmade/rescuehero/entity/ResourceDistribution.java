package tw.com.aidenmade.rescuehero.entity;

import jakarta.persistence.*;
import lombok.*;
import tw.com.aidenmade.rescuehero.entity.common.AuditInfo;

import java.time.Instant;

/**
 * 物資分配紀錄
 */
@Entity
@Table(name = "resource_distribution",
        indexes = {
                @Index(name = "idx_resource_dist_audit_id", columnList = "audit_id"),
                @Index(name = "idx_resource_dist_disaster_id", columnList = "disaster_id"),
                @Index(name = "idx_resource_dist_resource_id", columnList = "resource_id"),
                @Index(name = "idx_resource_dist_delivered_by", columnList = "delivered_by"),
                @Index(name = "idx_resource_dist_recipient_unit_id", columnList = "recipient_unit_id"),
                @Index(name = "idx_resource_dist_recipient_person_id", columnList = "recipient_person_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceDistribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditInfo auditInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disaster_id", nullable = false)
    private Disaster disaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivered_by", nullable = false)
    private RescueTeamMember deliveredBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_unit_id")
    private Unit recipientUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_person_id")
    private Person recipientPerson;

    @Column(name = "delivered_at", nullable = false)
    private Instant deliveredAt;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}
