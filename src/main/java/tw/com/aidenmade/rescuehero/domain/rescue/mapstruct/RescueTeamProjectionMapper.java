package tw.com.aidenmade.rescuehero.domain.rescue.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.StatusProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.UnitProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.utils.TimeMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueTeamDto;
import tw.com.aidenmade.rescuehero.domain.rescue.projection.RescueTeamMemberProjection;
import tw.com.aidenmade.rescuehero.domain.rescue.projection.RescueTeamProjection;

// 雖然 RescueTeamDto 並不直接接到 Instant，但是不知道為什麼還是被要求要加上 TimeMapper.class
@Mapper(componentModel = "spring", uses = {TimeMapper.class, AuditInfoProjectionMapper.class, UnitProjectionMapper.class, RescueGroupTaskProjectionMapper.class, StatusProjectionMapper.class, RescueTeamMemberProjection.class})
public interface RescueTeamProjectionMapper {
    RescueTeamProjectionMapper INSTANCE = Mappers.getMapper(RescueTeamProjectionMapper.class);
    RescueTeamDto toDto(RescueTeamProjection projection);
}
