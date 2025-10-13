package tw.com.aidenmade.rescuehero.domain.resource.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.resource.dto.ResourceTypeDto;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.resource.projection.ResourceTypeProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class})
public interface ResourceTypeProjectionMapper extends BaseProjectionMapper {
    ResourceTypeProjectionMapper INSTANCE = Mappers.getMapper(ResourceTypeProjectionMapper.class);
    ResourceTypeDto toDto(ResourceTypeProjection projection);
}
