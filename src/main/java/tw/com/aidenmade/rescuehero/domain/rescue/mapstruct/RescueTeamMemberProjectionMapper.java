package tw.com.aidenmade.rescuehero.domain.rescue.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.PersonProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueTeamMemberDto;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.projection.RescueTeamMemberProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, PersonProjectionMapper.class, RescueTeamProjectionMapper.class, RescueOrganizationProjectionMapper.class})
public interface RescueTeamMemberProjectionMapper extends BaseProjectionMapper {
    RescueTeamMemberProjectionMapper INSTANCE = Mappers.getMapper(RescueTeamMemberProjectionMapper.class);
    RescueTeamMemberDto toDto(RescueTeamMemberProjection projection);
}
