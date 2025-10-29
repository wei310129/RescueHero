package tw.com.aidenmade.rescuehero.domain.base.projection;

public interface CountryProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
    String getName();
    String getNativeName();
    String getCode();
}

