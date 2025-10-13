package tw.com.aidenmade.rescuehero.domain.resource.entity;

import jakarta.persistence.*;
import lombok.*;
import tw.com.aidenmade.rescuehero.domain.common.entity.AuditInfo;
import tw.com.aidenmade.rescuehero.domain.common.entity.Person;
import tw.com.aidenmade.rescuehero.domain.disaster.entity.Disaster;

import java.time.Instant;

/**
 * 物資需求表
 */
@Entity
@Table(name = "resource_request",
        indexes = {
                @Index(name = "idx_resource_request_audit_id", columnList = "audit_id"),
                @Index(name = "idx_resource_request_disaster_id", columnList = "disaster_id"),
                @Index(name = "idx_resource_request_resource_id", columnList = "resource_id"),
                @Index(name = "idx_resource_request_requested_by", columnList = "requested_by"),
                @Index(name = "idx_resource_request_fulfilled", columnList = "fulfilled")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceRequest {

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
    @JoinColumn(name = "requested_by")
    private Person requestedBy;

    @Column(name = "requested_at", nullable = false)
    private Instant requestedAt;

    @Builder.Default
    @Column(name = "fulfilled", nullable = false)
    private Boolean fulfilled = false;

    @Column(name = "fulfilled_at")
    private Instant fulfilledAt;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}
