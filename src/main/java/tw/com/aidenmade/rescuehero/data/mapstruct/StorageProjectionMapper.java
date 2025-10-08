package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.StorageProjection;
import tw.com.aidenmade.rescuehero.dto.StorageDto;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, CountryProjectionMapper.class, StorageTypeProjectionMapper.class, StatusProjectionMapper.class})
public interface StorageProjectionMapper extends BaseProjectionMapper {
    StorageProjectionMapper INSTANCE = Mappers.getMapper(StorageProjectionMapper.class);
    StorageDto toDto(StorageProjection projection);
}
