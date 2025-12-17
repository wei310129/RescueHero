package tw.com.aidenmade.rescuehero.domain.base.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tw.com.aidenmade.rescuehero.domain.base.application.service.RoleService;

@Slf4j
@Tag(name = "角色管理", description = "處理角色類型相關 API")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController extends AbstractBaseController {
    private final RoleService roleService;

    @Operation(summary = "取得帳號角色類型", description = "取得所有可用的帳號角色類型")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查詢成功")
    })
    @PermitAll
    @GetMapping("/account")
    public ResponseEntity<Object> getAccountRoles() {
        log.info("getAccountRoles");
        return okResponse(roleService.getAccountRoleTypes());
    }
}
