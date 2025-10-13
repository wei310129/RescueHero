package tw.com.aidenmade.rescuehero.domain.rescue.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.UnitProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.StatusProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueTeamDto;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.projection.RescueTeamProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, UnitProjectionMapper.class, RescueGroupProjectionMapper.class, StatusProjectionMapper.class})
public interface RescueTeamProjectionMapper extends BaseProjectionMapper {
    RescueTeamProjectionMapper INSTANCE = Mappers.getMapper(RescueTeamProjectionMapper.class);
    RescueTeamDto toDto(RescueTeamProjection projection);
}
