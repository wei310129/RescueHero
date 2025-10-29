package tw.com.aidenmade.rescuehero.domain.account.api.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 帳號非敏感資訊更新請求
 */
@Getter
@Setter
public class AccountUpdateAdminRequest {
    // ID
    private Long id;
    // 是否為管理員
    private Boolean isAdmin;
}
