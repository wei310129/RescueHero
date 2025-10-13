package tw.com.aidenmade.rescuehero.domain.household.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.UnitProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.disaster.mapstruct.DisasterProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.household.dto.HouseholdDto;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.StatusProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.household.projection.HouseholdProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, UnitProjectionMapper.class, DisasterProjectionMapper.class, StatusProjectionMapper.class})
public interface HouseholdProjectionMapper extends BaseProjectionMapper {
    HouseholdProjectionMapper INSTANCE = Mappers.getMapper(HouseholdProjectionMapper.class);
    HouseholdDto toDto(HouseholdProjection projection);
}
