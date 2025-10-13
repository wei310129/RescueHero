package tw.com.aidenmade.rescuehero.domain.common.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.CountryDto;
import tw.com.aidenmade.rescuehero.domain.common.projection.CountryProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class})
public interface CountryProjectionMapper extends BaseProjectionMapper {
    CountryProjectionMapper INSTANCE = Mappers.getMapper(CountryProjectionMapper.class);
    CountryDto toDto(CountryProjection projection);
}

