package tw.com.aidenmade.rescuehero.domain.account.api.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 帳號非敏感資訊更新請求
 */
@Getter
@Setter
public class AccountUpdateInsensitiveRequest {
    // ID
    private Long id;
    // 角色ID
    private Long roleId;
    // 電子郵件
    private String email;
    // 手機號碼
    private String phone;
    // 暱稱
    private String nickname;
}
