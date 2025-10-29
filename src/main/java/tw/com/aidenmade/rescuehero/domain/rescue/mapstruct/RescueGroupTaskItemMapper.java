package tw.com.aidenmade.rescuehero.domain.rescue.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.StatusProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueGroupTaskItemDto;
import tw.com.aidenmade.rescuehero.domain.rescue.projection.RescueGroupTaskItemProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, RescueGroupTaskProjectionMapper.class, StatusProjectionMapper.class})
public interface RescueGroupTaskItemMapper {
    RescueGroupTaskItemMapper INSTANCE = Mappers.getMapper(RescueGroupTaskItemMapper.class);
    RescueGroupTaskItemDto toDto(RescueGroupTaskItemProjection projection);
}
