package tw.com.aidenmade.rescuehero.domain.address.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.address.dto.AddressCellDto;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.address.projection.AddressCellProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, AddressLevelProjectionMapper.class})
public interface AddressCellProjectionMapper extends BaseProjectionMapper {
    AddressCellProjectionMapper INSTANCE = Mappers.getMapper(AddressCellProjectionMapper.class);
    AddressCellDto toDto(AddressCellProjection projection);
}

