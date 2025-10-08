package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.dto.ResourceRequestDto;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.ResourceRequestProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, DisasterProjectionMapper.class, ResourceProjectionMapper.class, PersonProjectionMapper.class})
public interface ResourceRequestProjectionMapper extends BaseProjectionMapper {
    ResourceRequestProjectionMapper INSTANCE = Mappers.getMapper(ResourceRequestProjectionMapper.class);
    ResourceRequestDto toDto(ResourceRequestProjection projection);
}
