package tw.com.aidenmade.rescuehero.domain.account.api.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 帳號新增請求
 */
@Getter
@Setter
public class AccountCreateRequest {
    // 角色ID
    private Long roleId;
    // 帳號
    private String username;
    // 密碼
    private String password;
    // 電子郵件
    private String email;
    // 手機號碼
//    private String phone;
    // Google OAuth2 ID
//    private String googleId;
    // 暱稱
//    private String nickname;
    // 驗證碼
    private String captcha;
}
