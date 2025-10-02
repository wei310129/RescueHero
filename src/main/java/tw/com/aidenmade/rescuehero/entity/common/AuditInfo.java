package tw.com.aidenmade.rescuehero.entity.common;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * 審計資訊基礎類別
 * 提供共用的審計欄位：id, created_at, updated_at, created_by
 */
@Entity
@Table(name = "audit_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;
}
