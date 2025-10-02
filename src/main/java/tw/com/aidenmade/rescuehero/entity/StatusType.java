package tw.com.aidenmade.rescuehero.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * 狀態類型表
 */
@Entity
@Table(name = "status_type",
        indexes = @Index(name = "idx_status_type_audit_id", columnList = "audit_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "audit_id", nullable = false)
    private UUID auditId;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
