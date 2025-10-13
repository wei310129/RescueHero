package tw.com.aidenmade.rescuehero.domain.storage.projection;

import tw.com.aidenmade.rescuehero.domain.address.application.dto.AddressDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.CountryDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.storage.application.dto.StorageTypeDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.AuditInfoDto;

public interface StorageProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    CountryDto getCountry();
    StorageTypeDto getStorageType();
    StatusDto getStatus();
    String getName();
    AddressDto getAddress();
    String getContactName();
    String getContactPhone();
    String getNote();
    Integer getCapacity();
}
