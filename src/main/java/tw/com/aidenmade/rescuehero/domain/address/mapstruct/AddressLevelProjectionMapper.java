package tw.com.aidenmade.rescuehero.domain.address.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.address.dto.AddressLevelDto;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.CountryProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.address.projection.AddressLevelProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, CountryProjectionMapper.class})
public interface AddressLevelProjectionMapper extends BaseProjectionMapper {
    AddressLevelProjectionMapper INSTANCE = Mappers.getMapper(AddressLevelProjectionMapper.class);
    AddressLevelDto toDto(AddressLevelProjection projection);
}

