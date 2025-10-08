package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.dto.HouseholdMemberDto;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.HouseholdMemberProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, PersonProjectionMapper.class, HouseholdProjectionMapper.class, StatusProjectionMapper.class})
public interface HouseholdMemberProjectionMapper extends BaseProjectionMapper {
    HouseholdMemberProjectionMapper INSTANCE = Mappers.getMapper(HouseholdMemberProjectionMapper.class);
    HouseholdMemberDto toDto(HouseholdMemberProjection projection);
}
