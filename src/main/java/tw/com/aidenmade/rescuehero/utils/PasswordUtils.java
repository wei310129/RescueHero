package tw.com.aidenmade.rescuehero.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtils {

    // 建立一個共用的 encoder 實例（只建立一次）
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    public PasswordUtils() {
        throw new AssertionError("No PasswordUtils instances for you!");
    }

    /**
     * 將明文密碼加密（含隨機 salt）
     */
    public static String encryptPassword(String rawPassword) {
        return ENCODER.encode(rawPassword);
    }

    /**
     * 驗證使用者輸入的密碼與資料庫雜湊是否一致
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return ENCODER.matches(rawPassword, encodedPassword);
    }
}
