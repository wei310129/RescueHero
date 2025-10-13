package tw.com.aidenmade.rescuehero.domain.resource.entity;

import jakarta.persistence.*;
import lombok.*;
import tw.com.aidenmade.rescuehero.domain.common.entity.AuditInfo;


/**
 * 物資種類表
 */
@Entity
@Table(name = "resource",
        uniqueConstraints = @UniqueConstraint(columnNames = {"type_id", "name"}),
        indexes = {
                @Index(name = "idx_resource_audit_id", columnList = "audit_id"),
                @Index(name = "idx_resource_type_id", columnList = "type_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditInfo auditInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private ResourceType resourceType;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "unit", nullable = false, length = 50)
    private String unit;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
