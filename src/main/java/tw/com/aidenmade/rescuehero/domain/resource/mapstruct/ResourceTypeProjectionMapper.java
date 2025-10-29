package tw.com.aidenmade.rescuehero.domain.resource.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.resource.application.dto.ResourceTypeDto;
import tw.com.aidenmade.rescuehero.domain.resource.projection.ResourceTypeProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class})
public interface ResourceTypeProjectionMapper {
    ResourceTypeProjectionMapper INSTANCE = Mappers.getMapper(ResourceTypeProjectionMapper.class);
    ResourceTypeDto toDto(ResourceTypeProjection projection);
}
