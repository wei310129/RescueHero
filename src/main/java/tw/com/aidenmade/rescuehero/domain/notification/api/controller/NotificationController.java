package tw.com.aidenmade.rescuehero.domain.notification.api.controller;

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
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController extends AbstractBaseController {

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
