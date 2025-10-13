package tw.com.aidenmade.rescuehero.domain.address.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.address.application.dto.AddressDto;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.CountryProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.address.projection.AddressProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, CountryProjectionMapper.class, AddressCellProjectionMapper.class})
public interface AddressProjectionMapper extends BaseProjectionMapper {
    AddressProjectionMapper INSTANCE = Mappers.getMapper(AddressProjectionMapper.class);
    AddressDto toDto(AddressProjection projection);
}

