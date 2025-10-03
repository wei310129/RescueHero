package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.StorageTypeProjection;
import tw.com.aidenmade.rescuehero.dto.StorageTypeDto;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class})
public interface StorageTypeProjectionMapper extends BaseProjectionMapper {
    StorageTypeProjectionMapper INSTANCE = Mappers.getMapper(StorageTypeProjectionMapper.class);
    StorageTypeDto toDto(StorageTypeProjection projection);
}
