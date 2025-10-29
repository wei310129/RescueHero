package tw.com.aidenmade.rescuehero.domain.account.api.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 帳號查詢
 */
@Getter
@Setter
public class AuthLoginRequest {
    private String username;
    private String password;
    private String captcha;
}

