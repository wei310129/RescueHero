package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.RescueGroupTaskProjection;
import tw.com.aidenmade.rescuehero.dto.RescueGroupTaskDto;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, RescueGroupProjectionMapper.class, DisasterProjectionMapper.class})
public interface RescueGroupTaskProjectionMapper extends BaseProjectionMapper {
    RescueGroupTaskProjectionMapper INSTANCE = Mappers.getMapper(RescueGroupTaskProjectionMapper.class);
    RescueGroupTaskDto toDto(RescueGroupTaskProjection projection);
}
