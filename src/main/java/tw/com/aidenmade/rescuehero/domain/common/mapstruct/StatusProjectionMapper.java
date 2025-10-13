package tw.com.aidenmade.rescuehero.domain.common.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.disaster.mapstruct.DisasterProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.projection.StatusProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, DisasterProjectionMapper.class, StatusTypeProjectionMapper.class})
public interface StatusProjectionMapper extends BaseProjectionMapper {
    StatusProjectionMapper INSTANCE = Mappers.getMapper(StatusProjectionMapper.class);
    StatusDto toDto(StatusProjection projection);
}
