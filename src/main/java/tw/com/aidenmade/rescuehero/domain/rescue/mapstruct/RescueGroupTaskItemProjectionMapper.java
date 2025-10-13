package tw.com.aidenmade.rescuehero.domain.rescue.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.StatusProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueGroupTaskItemDto;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.projection.RescueGroupTaskItemProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, RescueGroupTaskProjectionMapper.class, StatusProjectionMapper.class})
public interface RescueGroupTaskItemProjectionMapper extends BaseProjectionMapper {
    RescueGroupTaskItemProjectionMapper INSTANCE = Mappers.getMapper(RescueGroupTaskItemProjectionMapper.class);
    RescueGroupTaskItemDto toDto(RescueGroupTaskItemProjection projection);
}
