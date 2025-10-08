package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.dto.CountryDto;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.CountryProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class})
public interface CountryProjectionMapper extends BaseProjectionMapper {
    CountryProjectionMapper INSTANCE = Mappers.getMapper(CountryProjectionMapper.class);
    CountryDto toDto(CountryProjection projection);
}

