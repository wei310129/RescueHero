package tw.com.aidenmade.rescuehero.domain.rescue.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.StatusProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueGroupTaskItemDto;
import tw.com.aidenmade.rescuehero.domain.rescue.projection.RescueGroupTaskItemProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, RescueGroupTaskSimplifyMapper.class, StatusProjectionMapper.class})
public interface RescueGroupTaskItemProjectionMapper {
    RescueGroupTaskItemProjectionMapper INSTANCE = Mappers.getMapper(RescueGroupTaskItemProjectionMapper.class);
    @Mapping(target = "task", source = "task", qualifiedByName = "taskProjectionToId")
    RescueGroupTaskItemDto toDto(RescueGroupTaskItemProjection projection);
}
