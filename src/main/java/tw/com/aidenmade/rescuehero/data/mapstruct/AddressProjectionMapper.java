package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.dto.AddressDto;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.AddressProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, CountryProjectionMapper.class, AddressCellProjectionMapper.class})
public interface AddressProjectionMapper extends BaseProjectionMapper {
    AddressProjectionMapper INSTANCE = Mappers.getMapper(AddressProjectionMapper.class);
    AddressDto toDto(AddressProjection projection);
}

