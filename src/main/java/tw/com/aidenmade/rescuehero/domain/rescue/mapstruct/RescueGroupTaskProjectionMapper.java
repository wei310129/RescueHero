package tw.com.aidenmade.rescuehero.domain.rescue.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.disaster.mapstruct.DisasterProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueGroupTaskDto;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.projection.RescueGroupTaskProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, RescueGroupProjectionMapper.class, DisasterProjectionMapper.class})
public interface RescueGroupTaskProjectionMapper extends BaseProjectionMapper {
    RescueGroupTaskProjectionMapper INSTANCE = Mappers.getMapper(RescueGroupTaskProjectionMapper.class);
    RescueGroupTaskDto toDto(RescueGroupTaskProjection projection);
}
