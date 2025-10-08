package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.dto.AddressLevelDto;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.AddressLevelProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, CountryProjectionMapper.class})
public interface AddressLevelProjectionMapper extends BaseProjectionMapper {
    AddressLevelProjectionMapper INSTANCE = Mappers.getMapper(AddressLevelProjectionMapper.class);
    AddressLevelDto toDto(AddressLevelProjection projection);
}

