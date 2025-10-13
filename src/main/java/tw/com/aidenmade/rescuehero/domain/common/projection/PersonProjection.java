package tw.com.aidenmade.rescuehero.domain.common.projection;

import tw.com.aidenmade.rescuehero.domain.common.dto.CountryDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

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
