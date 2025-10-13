package tw.com.aidenmade.rescuehero.domain.common.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.StatusTypeDto;
import tw.com.aidenmade.rescuehero.domain.common.projection.StatusTypeProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class})
public interface StatusTypeProjectionMapper extends BaseProjectionMapper {
    StatusTypeProjectionMapper INSTANCE = Mappers.getMapper(StatusTypeProjectionMapper.class);
    StatusTypeDto toDto(StatusTypeProjection projection);
}
