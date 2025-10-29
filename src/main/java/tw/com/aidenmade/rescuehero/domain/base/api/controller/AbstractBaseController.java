package tw.com.aidenmade.rescuehero.domain.base.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public abstract class AbstractBaseController {
    protected final String SUCCESS = "success";
    protected final String ERROR = "error";

    protected ResponseEntity<Object> okResponse(Object object) {
        return ResponseEntity.ok(object);
    }

    protected ResponseEntity<Object> notFoundResponse() {
        return ResponseEntity.notFound().build();
    }

    /**
     * 用布林值判斷回傳成功/失敗
     */
    protected ResponseEntity<Object> booleanToCommonResponse(Boolean result, String actionName) {
        return result
                ? successResponseMsg(actionName)
                : errorResponseMsg(actionName);
    }

    protected ResponseEntity<Object> successResponseMsg(String actionName) {
        return ResponseEntity.ok(Map.of(SUCCESS, actionName + "成功"));
    }

    protected ResponseEntity<Object> errorResponseMsg(String errorMsg) {
        return ResponseEntity.badRequest().body(Map.of(ERROR, errorMsg + "失敗"));
    }

    protected ResponseEntity<Object> errorResponseMsg(String... errorMsgs) {
        List<String> errorMsgList = new ArrayList<>();
        if (errorMsgs != null) {
            for (String e : errorMsgs) {
                if (e == null || e.isBlank()) {
                    errorMsgList.add("未知錯誤");
                } else {
                    errorMsgList.add(e);
                }
            }
        }
        return ResponseEntity.badRequest().body(Map.of(ERROR, errorMsgList));
    }

    protected ResponseEntity<Object> authorizedResponse(String token) {
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
    }

    protected ResponseEntity<Object> unauthorizedResponse(String msg) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(Map.of(ERROR, msg));
    }
}
