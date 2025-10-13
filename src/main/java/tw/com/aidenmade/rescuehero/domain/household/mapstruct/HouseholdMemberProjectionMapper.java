package tw.com.aidenmade.rescuehero.domain.household.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.PersonProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.household.dto.HouseholdMemberDto;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.StatusProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.household.projection.HouseholdMemberProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, PersonProjectionMapper.class, HouseholdProjectionMapper.class, StatusProjectionMapper.class})
public interface HouseholdMemberProjectionMapper extends BaseProjectionMapper {
    HouseholdMemberProjectionMapper INSTANCE = Mappers.getMapper(HouseholdMemberProjectionMapper.class);
    HouseholdMemberDto toDto(HouseholdMemberProjection projection);
}
