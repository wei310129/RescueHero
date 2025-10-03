package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.ResourceTypeProjection;
import tw.com.aidenmade.rescuehero.dto.ResourceTypeDto;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class})
public interface ResourceTypeProjectionMapper extends BaseProjectionMapper {
    ResourceTypeProjectionMapper INSTANCE = Mappers.getMapper(ResourceTypeProjectionMapper.class);
    ResourceTypeDto toDto(ResourceTypeProjection projection);
}
