package tw.com.aidenmade.rescuehero.domain.notification.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tw.com.aidenmade.rescuehero.domain.base.api.controller.AbstractBaseController;
import tw.com.aidenmade.rescuehero.domain.notification.api.request.MarkReadRequest;

import java.util.List;

@Slf4j
@Tag(name = "通知管理", description = "處理用戶通知訊息相關 API")
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController extends AbstractBaseController {

    @Operation(summary = "取得用戶通知", description = "取得當前登入用戶的通知訊息列表")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查詢成功"),
            @ApiResponse(responseCode = "404", description = "找不到用戶資訊"),
            @ApiResponse(responseCode = "401", description = "未授權")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user")
    public ResponseEntity<Object> getUserNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            //TODO 用 username 取回真正要回傳的通知訊息，在這裡限制要回傳幾筆
            return okResponse(List.of(
                    "您有新任務.....",
                    "用戶「Somebody」對您傳送了訊息",
                    "您已經被「某某組織」分配到了「挖掘小組」"
                    ));
        } else {
            return notFoundResponse();
        }
    }

    @Operation(summary = "標記通知為已讀", description = "將指定的通知訊息標記為已讀")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "標記成功"),
            @ApiResponse(responseCode = "404", description = "找不到用戶資訊"),
            @ApiResponse(responseCode = "401", description = "未授權")
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mark-read")
    public ResponseEntity<Object> getMarkRead(@RequestBody MarkReadRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //TODO 根據 username 及 request 裡的 notificationIds 標記為已讀
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            request.getNotificationIds().forEach(id -> log.info("標記通知 {} 為已讀", id));
            return okResponse("標記成功");
        }
        log.error("通知標記已讀失敗，因為找不到使用者登入資訊");
        return notFoundResponse();
    }
}
