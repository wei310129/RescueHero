package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.dto.ResourceDto;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.ResourceProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, ResourceTypeProjectionMapper.class})
public interface ResourceProjectionMapper extends BaseProjectionMapper {
    ResourceProjectionMapper INSTANCE = Mappers.getMapper(ResourceProjectionMapper.class);
    ResourceDto toDto(ResourceProjection projection);
}
