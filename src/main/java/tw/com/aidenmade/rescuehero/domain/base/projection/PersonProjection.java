package tw.com.aidenmade.rescuehero.domain.base.projection;

public interface PersonProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
    CountryProjection getCountry();
    String getName();
    String getIdentification();
    Integer getAge();
    String getGender();
    String getPhone();
    String getEmail();
    String getNote();
}
