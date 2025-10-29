package tw.com.aidenmade.rescuehero.domain.account.api.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 帳號非敏感資訊更新請求
 */
@Getter
@Setter
public class AccountUpdatePasswordRequest {
    // ID
    private Long id;
    // 密碼
    private String password;
}
