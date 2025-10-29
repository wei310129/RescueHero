package tw.com.aidenmade.rescuehero.domain.base.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tw.com.aidenmade.rescuehero.domain.base.application.service.RoleService;

@Slf4j
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController extends AbstractBaseController {
    private final RoleService roleService;

    @GetMapping("/account")
    public ResponseEntity<Object> getAccountRoles() {
        log.info("getAccountRoles");
        return okResponse(roleService.getAccountRoleTypes());
    }
}
