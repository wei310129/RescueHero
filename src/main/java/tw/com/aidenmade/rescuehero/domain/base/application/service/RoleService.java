package tw.com.aidenmade.rescuehero.domain.base.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.RoleDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.RoleTypeDto;
import tw.com.aidenmade.rescuehero.domain.base.enums.Role;
import tw.com.aidenmade.rescuehero.domain.base.enums.RoleType;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.RoleProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.RoleTypeProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.repository.RoleRepository;
import tw.com.aidenmade.rescuehero.domain.base.repository.RoleTypeRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleTypeRepository roleTypeRepository;
    private final RoleTypeProjectionMapper roleTypeProjectionMapper;

    private final RoleRepository roleRepository;
    private final RoleProjectionMapper roleProjectionMapper;

    @Transactional
    public List<RoleDto> getAccountRoleTypes() {
        RoleTypeDto accountRoleType = roleTypeProjectionMapper
                .toDto(roleTypeRepository.findByName(RoleType.ACCOUNT.name()));
        return roleRepository.findByRoleTypeId(accountRoleType.id()).stream()
                .map(roleProjectionMapper::toDto)
                .filter(r -> !Role.ROLE_ADMIN.name().equals(r.name()))
                .toList();
    }
}
