package tw.com.aidenmade.rescuehero.domain.address.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.address.application.dto.AddressCellDto;
import tw.com.aidenmade.rescuehero.domain.address.projection.AddressCellProjection;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.AuditInfoProjectionMapper;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, AddressLevelProjectionMapper.class})
public interface AddressCellProjectionMapper {
    AddressCellProjectionMapper INSTANCE = Mappers.getMapper(AddressCellProjectionMapper.class);
    AddressCellDto toDto(AddressCellProjection projection);
}

