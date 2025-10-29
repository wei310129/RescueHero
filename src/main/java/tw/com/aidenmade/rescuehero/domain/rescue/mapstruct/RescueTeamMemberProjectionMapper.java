package tw.com.aidenmade.rescuehero.domain.rescue.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.PersonProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueTeamMemberDto;
import tw.com.aidenmade.rescuehero.domain.rescue.projection.RescueTeamMemberProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, PersonProjectionMapper.class, RescueTeamProjectionMapper.class, RescueOrganizationProjectionMapper.class})
public interface RescueTeamMemberProjectionMapper {
    RescueTeamMemberProjectionMapper INSTANCE = Mappers.getMapper(RescueTeamMemberProjectionMapper.class);
    RescueTeamMemberDto toDto(RescueTeamMemberProjection projection);
}
