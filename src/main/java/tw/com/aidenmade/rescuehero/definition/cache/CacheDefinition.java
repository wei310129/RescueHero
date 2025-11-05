package tw.com.aidenmade.rescuehero.definition.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tw.com.aidenmade.rescuehero.enums.TTLType;

import static tw.com.aidenmade.rescuehero.enums.TTLType.DAY_7;
import static tw.com.aidenmade.rescuehero.enums.TTLType.MINUTE_5;

/**
 * Cache定義清單
 *
 */
@Getter
@AllArgsConstructor
public enum CacheDefinition {
    // JWT
    JWT_ACCESS_TOKEN(CacheName.JWT_ACCESS_TOKEN, MINUTE_5),
    JWT_REFRESH_TOKEN(CacheName.JWT_REFRESH_TOKEN, DAY_7),


    ;

    // 在 RedisConfig 初始化時會檢查 name 有沒有重複
    private final String name;
    private final TTLType ttlType;
}
