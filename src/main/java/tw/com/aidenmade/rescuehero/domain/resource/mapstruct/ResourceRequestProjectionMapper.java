package tw.com.aidenmade.rescuehero.domain.resource.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.PersonProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.disaster.mapstruct.DisasterProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.resource.dto.ResourceRequestDto;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.resource.projection.ResourceRequestProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, DisasterProjectionMapper.class, ResourceProjectionMapper.class, PersonProjectionMapper.class})
public interface ResourceRequestProjectionMapper extends BaseProjectionMapper {
    ResourceRequestProjectionMapper INSTANCE = Mappers.getMapper(ResourceRequestProjectionMapper.class);
    ResourceRequestDto toDto(ResourceRequestProjection projection);
}
