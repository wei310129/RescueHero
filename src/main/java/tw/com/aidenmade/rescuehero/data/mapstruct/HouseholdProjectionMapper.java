package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.dto.HouseholdDto;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.HouseholdProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, UnitProjectionMapper.class, DisasterProjectionMapper.class, StatusProjectionMapper.class})
public interface HouseholdProjectionMapper extends BaseProjectionMapper {
    HouseholdProjectionMapper INSTANCE = Mappers.getMapper(HouseholdProjectionMapper.class);
    HouseholdDto toDto(HouseholdProjection projection);
}
