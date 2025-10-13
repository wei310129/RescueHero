package tw.com.aidenmade.rescuehero.domain.storage.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.CountryProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.StatusProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.storage.application.dto.StorageDto;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.storage.projection.StorageProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, CountryProjectionMapper.class, StorageTypeProjectionMapper.class, StatusProjectionMapper.class})
public interface StorageProjectionMapper extends BaseProjectionMapper {
    StorageProjectionMapper INSTANCE = Mappers.getMapper(StorageProjectionMapper.class);
    StorageDto toDto(StorageProjection projection);
}
