package tw.com.aidenmade.rescuehero.domain.household.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.PersonProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.StatusProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.household.application.dto.HouseholdMemberDto;
import tw.com.aidenmade.rescuehero.domain.household.projection.HouseholdMemberProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, PersonProjectionMapper.class, HouseholdProjectionMapper.class, StatusProjectionMapper.class})
public interface HouseholdMemberMapper {
    HouseholdMemberMapper INSTANCE = Mappers.getMapper(HouseholdMemberMapper.class);
    HouseholdMemberDto toDto(HouseholdMemberProjection projection);
}
