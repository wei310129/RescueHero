package tw.com.aidenmade.rescuehero.domain.rescue.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.StatusProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.disaster.mapstruct.DisasterProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueGroupTaskDto;
import tw.com.aidenmade.rescuehero.domain.rescue.projection.RescueGroupTaskProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, RescueGroupProjectionMapper.class, DisasterProjectionMapper.class, StatusProjectionMapper.class, RescueGroupTaskItemProjectionMapper.class})
public interface RescueGroupTaskProjectionMapper {
    RescueGroupTaskProjectionMapper INSTANCE = Mappers.getMapper(RescueGroupTaskProjectionMapper.class);
    RescueGroupTaskDto toDto(RescueGroupTaskProjection projection);
}
