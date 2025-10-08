package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.dto.RescueGroupTaskItemDto;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.RescueGroupTaskItemProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, RescueGroupTaskProjectionMapper.class, StatusProjectionMapper.class})
public interface RescueGroupTaskItemProjectionMapper extends BaseProjectionMapper {
    RescueGroupTaskItemProjectionMapper INSTANCE = Mappers.getMapper(RescueGroupTaskItemProjectionMapper.class);
    RescueGroupTaskItemDto toDto(RescueGroupTaskItemProjection projection);
}
