package tw.com.aidenmade.rescuehero.domain.resource.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.PersonProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.disaster.mapstruct.DisasterProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.resource.application.dto.ResourceRequestDto;
import tw.com.aidenmade.rescuehero.domain.resource.projection.ResourceRequestProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, DisasterProjectionMapper.class, ResourceProjectionMapper.class, PersonProjectionMapper.class})
public interface ResourceRequestProjectionMapper {
    ResourceRequestProjectionMapper INSTANCE = Mappers.getMapper(ResourceRequestProjectionMapper.class);
    ResourceRequestDto toDto(ResourceRequestProjection projection);
}
