package tw.com.aidenmade.rescuehero.domain.base.projection;

public interface RoleProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
    RoleTypeProjection getRoleType();
    String getName();
    String getDescription();
}
