package tw.com.aidenmade.rescuehero.domain.rescue.api.controller;

import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tw.com.aidenmade.rescuehero.domain.base.api.controller.AbstractBaseController;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.base.application.service.StatusService;
import tw.com.aidenmade.rescuehero.domain.base.definition.StatusDefinition;
import tw.com.aidenmade.rescuehero.domain.rescue.api.request.RescueGroupTaskAcceptedRequest;
import tw.com.aidenmade.rescuehero.domain.rescue.api.request.RescueGroupTaskAvailableRequest;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueGroupTaskDto;
import tw.com.aidenmade.rescuehero.domain.rescue.application.service.RescueGroupTaskService;

@Slf4j
@Tag(name = "救援任務管理", description = "處理救援群組任務相關 API")
@RestController
@RequestMapping("/group-task")
@RequiredArgsConstructor
public class RescueGroupTaskController extends AbstractBaseController {
    private final StatusService statusService;
    private final RescueGroupTaskService rescueGroupTaskService;

    @Operation(summary = "取得可用任務", description = "查詢用戶可接取的救援任務列表")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查詢成功"),
            @ApiResponse(responseCode = "404", description = "查無可用任務"),
            @ApiResponse(responseCode = "401", description = "未授權"),
            @ApiResponse(responseCode = "403", description = "權限不足")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/available")
    public ResponseEntity<Object> getUserAvailable(@RequestBody RescueGroupTaskAvailableRequest request) {
        StatusDto statusInProgress;
        try {
            statusInProgress = getStatusDtoByCode(StatusDefinition.TASK_IN_PROGRESS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return notFoundResponse();
        }

        Page<RescueGroupTaskDto> page = rescueGroupTaskService.getUserAvailable(
                request.getNameLike(),
                statusInProgress.id(),
                request.getPriority(),
                convertToPageable(request.getPage())
        );
        if (!page.hasContent()) {
            return notFoundResponse();
        }
        return okResponse(page);
    }

    @Operation(summary = "取得已接受任務", description = "查詢用戶已接取的救援任務列表")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查詢成功"),
            @ApiResponse(responseCode = "404", description = "查無已接受任務"),
            @ApiResponse(responseCode = "401", description = "未授權"),
            @ApiResponse(responseCode = "403", description = "權限不足")
    })
    // TODO: 此 api 尚未有前端呼叫過，待測
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/accepted")
    public ResponseEntity<Object> getUserAccepted(@RequestBody RescueGroupTaskAcceptedRequest request) {
        Page<RescueGroupTaskDto> page = rescueGroupTaskService.getUserAccepted(
                request.getNameLike(),
                request.getPriority(),
                convertToPageable(request.getPage())
        );
        if (!page.hasContent()) {
            return notFoundResponse();
        }
        return okResponse(page);
    }

    private StatusDto getStatusDtoByCode(StatusDefinition statusDefinition) {
        String statusName = statusDefinition.getStatusName();
        return statusService.getTaskStatuses().stream()
                .filter(s -> StringUtils.isBlank(statusName) || s.code().equals(statusName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("找不到任務類型: {" + statusName + "}，若為空則代表找不到任何 status"));
    }
}
