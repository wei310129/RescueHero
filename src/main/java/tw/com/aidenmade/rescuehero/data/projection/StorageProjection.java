package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.dto.AddressDto;
import tw.com.aidenmade.rescuehero.dto.CountryDto;
import tw.com.aidenmade.rescuehero.dto.StatusDto;
import tw.com.aidenmade.rescuehero.dto.StorageTypeDto;
import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;

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
