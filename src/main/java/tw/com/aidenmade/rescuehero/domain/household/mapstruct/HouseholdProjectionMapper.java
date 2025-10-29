package tw.com.aidenmade.rescuehero.domain.household.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.StatusProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.UnitProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.disaster.mapstruct.DisasterProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.household.application.dto.HouseholdDto;
import tw.com.aidenmade.rescuehero.domain.household.projection.HouseholdProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, UnitProjectionMapper.class, DisasterProjectionMapper.class, StatusProjectionMapper.class})
public interface HouseholdProjectionMapper {
    HouseholdProjectionMapper INSTANCE = Mappers.getMapper(HouseholdProjectionMapper.class);
    HouseholdDto toDto(HouseholdProjection projection);
}
