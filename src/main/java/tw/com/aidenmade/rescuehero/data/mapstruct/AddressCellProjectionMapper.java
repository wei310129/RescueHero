package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.dto.AddressCellDto;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.AddressCellProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, AddressLevelProjectionMapper.class})
public interface AddressCellProjectionMapper extends BaseProjectionMapper {
    AddressCellProjectionMapper INSTANCE = Mappers.getMapper(AddressCellProjectionMapper.class);
    AddressCellDto toDto(AddressCellProjection projection);
}

