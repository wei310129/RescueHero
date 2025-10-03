package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;

public interface PersonProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    String getName();
    String getIdentification();
    Integer getAge();
    String getGender();
    String getPhone();
    String getEmail();
    String getNote();
}
