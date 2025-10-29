package tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.StatusTypeDto;
import tw.com.aidenmade.rescuehero.domain.base.projection.StatusTypeProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class})
public interface StatusTypeProjectionMapper {
    StatusTypeProjectionMapper INSTANCE = Mappers.getMapper(StatusTypeProjectionMapper.class);
    StatusTypeDto toDto(StatusTypeProjection projection);
}
