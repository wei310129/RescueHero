package tw.com.aidenmade.rescuehero.entity;

import jakarta.persistence.*;
import lombok.*;
import tw.com.aidenmade.rescuehero.entity.common.AuditInfo;


/**
 * 物資類型表
 */
@Entity
@Table(name = "resource_type",
        indexes = @Index(name = "idx_resource_type_audit_id", columnList = "audit_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditInfo auditInfo;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
