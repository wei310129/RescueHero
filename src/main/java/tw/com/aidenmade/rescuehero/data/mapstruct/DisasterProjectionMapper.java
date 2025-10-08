package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.DisasterProjection;
import tw.com.aidenmade.rescuehero.dto.DisasterDto;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, CountryProjectionMapper.class, AddressProjectionMapper.class})
public interface DisasterProjectionMapper extends BaseProjectionMapper {
    DisasterProjectionMapper INSTANCE = Mappers.getMapper(DisasterProjectionMapper.class);
    DisasterDto toDto(DisasterProjection projection);
}
