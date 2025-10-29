package tw.com.aidenmade.rescuehero.domain.base.projection;

public interface RoleTypeProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
    String getName();
    String getDescription();
}

