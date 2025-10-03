package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.PersonProjection;
import tw.com.aidenmade.rescuehero.dto.PersonDto;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class})
public interface PersonProjectionMapper extends BaseProjectionMapper {
    PersonProjectionMapper INSTANCE = Mappers.getMapper(PersonProjectionMapper.class);
    PersonDto toDto(PersonProjection projection);
}
