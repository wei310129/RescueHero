package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.RescueTeamProjection;
import tw.com.aidenmade.rescuehero.dto.RescueTeamDto;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, UnitProjectionMapper.class, RescueGroupProjectionMapper.class, StatusProjectionMapper.class})
public interface RescueTeamProjectionMapper extends BaseProjectionMapper {
    RescueTeamProjectionMapper INSTANCE = Mappers.getMapper(RescueTeamProjectionMapper.class);
    RescueTeamDto toDto(RescueTeamProjection projection);
}
