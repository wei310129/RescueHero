package tw.com.aidenmade.rescuehero.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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

    @Column(name = "audit_id", nullable = false)
    private UUID auditId;

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
