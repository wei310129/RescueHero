package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.CountryDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

public interface PersonProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    CountryDto getCountry();
    String getName();
    String getIdentification();
    Integer getAge();
    String getGender();
    String getPhone();
    String getEmail();
    String getNote();
}
