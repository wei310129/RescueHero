package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.dto.StorageInventoryDto;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.StorageInventoryProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, DisasterProjectionMapper.class, StorageProjectionMapper.class, ResourceProjectionMapper.class})
public interface StorageInventoryProjectionMapper extends BaseProjectionMapper {
    StorageInventoryProjectionMapper INSTANCE = Mappers.getMapper(StorageInventoryProjectionMapper.class);
    StorageInventoryDto toDto(StorageInventoryProjection projection);
}
