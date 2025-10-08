package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.dto.RescueTeamMemberDto;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.RescueTeamMemberProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, PersonProjectionMapper.class, RescueTeamProjectionMapper.class, RescueOrganizationProjectionMapper.class})
public interface RescueTeamMemberProjectionMapper extends BaseProjectionMapper {
    RescueTeamMemberProjectionMapper INSTANCE = Mappers.getMapper(RescueTeamMemberProjectionMapper.class);
    RescueTeamMemberDto toDto(RescueTeamMemberProjection projection);
}
