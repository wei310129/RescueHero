package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.StatusProjection;
import tw.com.aidenmade.rescuehero.dto.StatusDto;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, DisasterProjectionMapper.class, StatusTypeProjectionMapper.class})
public interface StatusProjectionMapper extends BaseProjectionMapper {
    StatusProjectionMapper INSTANCE = Mappers.getMapper(StatusProjectionMapper.class);
    StatusDto toDto(StatusProjection projection);
}
