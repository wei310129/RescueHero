package tw.com.aidenmade.rescuehero.domain.base.projection;

public interface StatusTypeProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
    String getName();
    String getDescription();
}

