package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.dto.StatusTypeDto;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.StatusTypeProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class})
public interface StatusTypeProjectionMapper extends BaseProjectionMapper {
    StatusTypeProjectionMapper INSTANCE = Mappers.getMapper(StatusTypeProjectionMapper.class);
    StatusTypeDto toDto(StatusTypeProjection projection);
}
