package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.dto.PersonDto;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.PersonProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, CountryProjectionMapper.class})
public interface PersonProjectionMapper extends BaseProjectionMapper {
    PersonProjectionMapper INSTANCE = Mappers.getMapper(PersonProjectionMapper.class);
    PersonDto toDto(PersonProjection projection);
}
