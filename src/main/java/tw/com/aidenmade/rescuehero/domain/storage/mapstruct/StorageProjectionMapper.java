package tw.com.aidenmade.rescuehero.domain.storage.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.CountryProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.StatusProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.storage.application.dto.StorageDto;
import tw.com.aidenmade.rescuehero.domain.storage.projection.StorageProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, CountryProjectionMapper.class, StorageTypeMapper.class, StatusProjectionMapper.class})
public interface StorageProjectionMapper {
    StorageProjectionMapper INSTANCE = Mappers.getMapper(StorageProjectionMapper.class);
    StorageDto toDto(StorageProjection projection);
}
