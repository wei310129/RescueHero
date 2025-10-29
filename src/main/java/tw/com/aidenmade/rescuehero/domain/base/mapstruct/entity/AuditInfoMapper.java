package tw.com.aidenmade.rescuehero.domain.base.mapstruct.entity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tw.com.aidenmade.rescuehero.domain.account.mapstruct.AccountSimplifyMapper;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.base.entity.AuditInfo;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.utils.TimeMapper;

@Mapper(componentModel = "spring", uses = {TimeMapper.class, AccountSimplifyMapper.class})
public interface AuditInfoMapper {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "accountToId")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "accountToId")
    AuditInfoDto toDto(AuditInfo entity);

}
