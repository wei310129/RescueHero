package tw.com.aidenmade.rescuehero.domain.account.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import tw.com.aidenmade.rescuehero.domain.base.entity.AuditInfo;
import tw.com.aidenmade.rescuehero.domain.base.entity.Role;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("主鍵 ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditInfo auditInfo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    @Comment("角色")
    private Role role;

    @Column(name = "username", nullable = false, unique = true, length = 64)
    @Comment("帳號")
    private String username;

    @Column(name = "password_hash", length = 255)
    @Comment("密碼雜湊 (OAuth2 可為 NULL)")
    private String passwordHash;

    @Column(name = "email", nullable = false, unique = true, length = 128)
    @Comment("電子郵件")
    private String email;

    @Column(name = "phone", length = 32)
    @Comment("手機號碼")
    private String phone;

    @Column(name = "google_id", unique = true, length = 64)
    @Comment("Google OAuth2 ID")
    private String googleId;

    @Column(name = "nickname", length = 64)
    @Comment("暱稱")
    private String nickname;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    @Comment("啟用狀態")
    private Boolean isActive = true;

    @Builder.Default
    @Column(name = "is_admin", nullable = false)
    @Comment("是否為管理員")
    private Boolean isAdmin = false;
}
