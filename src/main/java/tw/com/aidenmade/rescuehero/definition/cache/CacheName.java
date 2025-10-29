package tw.com.aidenmade.rescuehero.definition.cache;

/**
 * 由於 @Cacheable 內的參數必須是編譯時就要取得，因此若要集中管理必須存成 String 等基本型別
 * 本介面用於指定給 @Cacheable value 參數用
 */
public interface CacheName {
    String JWT_ACCESS_TOKEN = "JWT_ACCESS_TOKEN";
    String JWT_REFRESH_TOKEN = "JWT_REFRESH_TOKEN";
}
