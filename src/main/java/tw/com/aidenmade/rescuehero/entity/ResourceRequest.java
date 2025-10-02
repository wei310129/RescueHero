package tw.com.aidenmade.rescuehero.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

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

    @Column(name = "audit_id", nullable = false)
    private UUID auditId;

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
    private RescueTeamMember requestedBy;

    @Column(name = "requested_at", nullable = false)
    private Instant requestedAt;

    @Column(name = "fulfilled", nullable = false)
    private Boolean fulfilled = false;

    @Column(name = "fulfilled_at")
    private Instant fulfilledAt;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}
