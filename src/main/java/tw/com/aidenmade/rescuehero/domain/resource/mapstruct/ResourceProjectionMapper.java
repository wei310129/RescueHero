package tw.com.aidenmade.rescuehero.domain.resource.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.resource.application.dto.ResourceDto;
import tw.com.aidenmade.rescuehero.domain.resource.projection.ResourceProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, ResourceTypeProjectionMapper.class})
public interface ResourceProjectionMapper {
    ResourceProjectionMapper INSTANCE = Mappers.getMapper(ResourceProjectionMapper.class);
    ResourceDto toDto(ResourceProjection projection);
}
