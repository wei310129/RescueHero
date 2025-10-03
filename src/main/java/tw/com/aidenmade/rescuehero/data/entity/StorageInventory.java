package tw.com.aidenmade.rescuehero.data.entity;

import jakarta.persistence.*;
import lombok.*;
import tw.com.aidenmade.rescuehero.data.entity.common.AuditInfo;


/**
 * 每個儲存站的物資庫存
 */
@Entity
@Table(name = "storage_inventory",
        uniqueConstraints = @UniqueConstraint(columnNames = {"storage_id", "resource_id"}),
        indexes = {
                @Index(name = "idx_storage_inventory_audit_id", columnList = "audit_id"),
                @Index(name = "idx_storage_inventory_disaster_id", columnList = "disaster_id"),
                @Index(name = "idx_storage_inventory_storage_id", columnList = "storage_id"),
                @Index(name = "idx_storage_inventory_resource_id", columnList = "resource_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorageInventory {

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
    @JoinColumn(name = "storage_id", nullable = false)
    private Storage storage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
