package tw.com.aidenmade.rescuehero.entity;

import jakarta.persistence.*;
import lombok.*;
import tw.com.aidenmade.rescuehero.entity.common.AuditInfo;


/**
 * 儲存站類型表
 */
@Entity
@Table(name = "storage_type",
        indexes = @Index(name = "idx_storage_type_audit_id", columnList = "audit_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorageType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditInfo auditInfo;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}