package tw.com.aidenmade.rescuehero.domain.common.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.UnitDto;
import tw.com.aidenmade.rescuehero.domain.common.projection.UnitProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, CountryProjectionMapper.class})
public interface UnitProjectionMapper extends BaseProjectionMapper {
    UnitProjectionMapper INSTANCE = Mappers.getMapper(UnitProjectionMapper.class);
    UnitDto toDto(UnitProjection projection);
}
