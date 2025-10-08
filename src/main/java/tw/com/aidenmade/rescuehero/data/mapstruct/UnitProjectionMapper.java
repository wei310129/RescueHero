package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.UnitProjection;
import tw.com.aidenmade.rescuehero.dto.UnitDto;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, CountryProjectionMapper.class})
public interface UnitProjectionMapper extends BaseProjectionMapper {
    UnitProjectionMapper INSTANCE = Mappers.getMapper(UnitProjectionMapper.class);
    UnitDto toDto(UnitProjection projection);
}
