package tw.com.aidenmade.rescuehero.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "status", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"disaster_id", "type_id", "code"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("主鍵 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disaster_id", nullable = false)
    @Comment("所屬災害")
    private Disaster disaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    @Comment("狀態類型 (status_type 外鍵)")
    private StatusType type;

    @Column(nullable = false, length = 50)
    @Comment("狀態代碼 (ex: pending, completed)")
    private String code;

    @Column(nullable = false, length = 100)
    @Comment("顯示名稱 (例: 待處理, 已完成)")
    private String name;

    @Column(columnDefinition = "TEXT")
    @Comment("狀態說明")
    private String description;
}


