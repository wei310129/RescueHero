package tw.com.aidenmade.rescuehero.domain.account.projection;

import tw.com.aidenmade.rescuehero.domain.base.projection.AuditInfoProjection;
import tw.com.aidenmade.rescuehero.domain.base.projection.RoleProjection;

public interface AccountProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
    RoleProjection getRole();
    String getUsername();
    String getPasswordHash();
    String getEmail();
    String getPhone();
    String getGoogleId();
    String getNickname();
    Boolean getIsActive();
    Boolean getIsAdmin();
}

