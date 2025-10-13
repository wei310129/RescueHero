package tw.com.aidenmade.rescuehero.domain.storage.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.disaster.mapstruct.DisasterProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.resource.mapstruct.ResourceProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.storage.dto.StorageInventoryDto;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.storage.projection.StorageInventoryProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, DisasterProjectionMapper.class, StorageProjectionMapper.class, ResourceProjectionMapper.class})
public interface StorageInventoryProjectionMapper extends BaseProjectionMapper {
    StorageInventoryProjectionMapper INSTANCE = Mappers.getMapper(StorageInventoryProjectionMapper.class);
    StorageInventoryDto toDto(StorageInventoryProjection projection);
}
