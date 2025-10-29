package tw.com.aidenmade.rescuehero.domain.account.api.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 帳號啟用狀態更新
 */
@Getter
@Setter
public class AccountUpdateActiveRequest {
    // ID
    private Long id;
    // 啟用狀態
    private Boolean isActive;
}
