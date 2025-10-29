package tw.com.aidenmade.rescuehero.domain.address.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.address.application.dto.AddressLevelDto;
import tw.com.aidenmade.rescuehero.domain.address.projection.AddressLevelProjection;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.CountryProjectionMapper;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, CountryProjectionMapper.class})
public interface AddressLevelProjectionMapper {
    AddressLevelProjectionMapper INSTANCE = Mappers.getMapper(AddressLevelProjectionMapper.class);
    AddressLevelDto toDto(AddressLevelProjection projection);
}

