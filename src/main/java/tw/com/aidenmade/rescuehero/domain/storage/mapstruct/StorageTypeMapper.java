package tw.com.aidenmade.rescuehero.domain.storage.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.storage.application.dto.StorageTypeDto;
import tw.com.aidenmade.rescuehero.domain.storage.projection.StorageTypeProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class})
public interface StorageTypeMapper {
    StorageTypeMapper INSTANCE = Mappers.getMapper(StorageTypeMapper.class);
    StorageTypeDto toDto(StorageTypeProjection projection);
}
