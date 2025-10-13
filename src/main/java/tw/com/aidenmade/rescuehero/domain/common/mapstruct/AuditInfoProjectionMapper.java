package tw.com.aidenmade.rescuehero.domain.common.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.common.projection.AuditInfoProjection;

@Mapper(componentModel = "spring")
public interface AuditInfoProjectionMapper extends BaseProjectionMapper {
    AuditInfoProjectionMapper INSTANCE = Mappers.getMapper(AuditInfoProjectionMapper.class);
    AuditInfoDto toDto(AuditInfoProjection projection);
}
