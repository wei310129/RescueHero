package tw.com.aidenmade.rescuehero.domain.storage.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.storage.dto.StorageTypeDto;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.storage.projection.StorageTypeProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class})
public interface StorageTypeProjectionMapper extends BaseProjectionMapper {
    StorageTypeProjectionMapper INSTANCE = Mappers.getMapper(StorageTypeProjectionMapper.class);
    StorageTypeDto toDto(StorageTypeProjection projection);
}
