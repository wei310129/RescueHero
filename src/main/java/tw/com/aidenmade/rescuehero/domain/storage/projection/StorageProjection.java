package tw.com.aidenmade.rescuehero.domain.storage.projection;

import tw.com.aidenmade.rescuehero.domain.address.dto.AddressDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.CountryDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.storage.dto.StorageTypeDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

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
