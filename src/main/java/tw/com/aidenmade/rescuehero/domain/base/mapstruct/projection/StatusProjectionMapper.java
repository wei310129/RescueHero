package tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.base.projection.StatusProjection;
import tw.com.aidenmade.rescuehero.domain.disaster.mapstruct.DisasterProjectionMapper;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, DisasterProjectionMapper.class, StatusTypeProjectionMapper.class})
public interface StatusProjectionMapper {
    StatusProjectionMapper INSTANCE = Mappers.getMapper(StatusProjectionMapper.class);
    StatusDto toDto(StatusProjection projection);
}
