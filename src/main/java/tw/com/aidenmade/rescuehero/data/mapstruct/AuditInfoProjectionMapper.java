package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.AuditInfoProjection;

@Mapper(componentModel = "spring")
public interface AuditInfoProjectionMapper extends BaseProjectionMapper {
    AuditInfoProjectionMapper INSTANCE = Mappers.getMapper(AuditInfoProjectionMapper.class);
    AuditInfoDto toDto(AuditInfoProjection projection);
}
