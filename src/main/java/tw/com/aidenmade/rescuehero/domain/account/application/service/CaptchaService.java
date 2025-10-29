package tw.com.aidenmade.rescuehero.domain.account.application.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaService {
    private static final String SESSION_KEY = "CAPTCHA_CODE";
    private static final int WIDTH = 150;
    private static final int HEIGHT = 48;
    private static final String CHARS = "ABCDEFGHJKLMNQRTUYabcdefghijkmnqrtuy23456789"; // 可使用的文字，避免使用易誤認的字元
    private static final long CAPTCHA_TTL_MILLIS = 3 * 60 * 1000; // 3分鐘

    // CaptchaInfo: 存 code 與 timestamp
    public record CaptchaInfo(String code, long timestamp) implements Serializable {}

    public boolean getCaptcha(HttpSession session, HttpServletResponse response) {
        try {
            String code = randomCode(5);
            long now = System.currentTimeMillis();
            session.setAttribute(SESSION_KEY, new CaptchaInfo(code, now));

            BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();

            // 背景
            g.setColor(new Color(230, 230, 250));
            g.fillRect(0, 0, WIDTH, HEIGHT);

            // 雜線干擾
            Random rnd = new Random();
            for (int i = 0; i < 20; i++) {
                g.setColor(randomColor(rnd));
                int x1 = rnd.nextInt(WIDTH), y1 = rnd.nextInt(HEIGHT);
                int x2 = rnd.nextInt(WIDTH), y2 = rnd.nextInt(HEIGHT);
                g.drawLine(x1, y1, x2, y2);
            }

            // 驗證碼字串
            g.setFont(new Font("Arial", Font.BOLD, 32));
            for (int i = 0; i < code.length(); i++) {
                g.setColor(randomColor(rnd));
                int x = 18 + i * 26;
                int y = 30 + rnd.nextInt(8) - 4;
                double rotate = (rnd.nextDouble() - 0.5) * 0.4;
                g.rotate(rotate, x, y);
                g.drawString(String.valueOf(code.charAt(i)), x, y);
                g.rotate(-rotate, x, y);
            }

            g.dispose();
            response.setContentType("image/png");
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            ImageIO.write(image, "png", response.getOutputStream());
            return true;
        } catch (IOException e) {
            log.error("驗證碼生成發生錯誤");
            return false;
        }
    }

    public Boolean verifyCaptcha(String inputCaptcha, HttpSession session) {
        Object obj = session.getAttribute(SESSION_KEY);
        if (!(obj instanceof CaptchaInfo info)) return false;
        long now = System.currentTimeMillis();
        // 過期
        if (now - info.timestamp > CAPTCHA_TTL_MILLIS) {
            removeCaptcha(session);
            return null;
        }
        // 輸入錯誤，不移除，可重試
        if (!info.code.equals(inputCaptcha)) {
            return false;
        }
        return true;
    }

    public void removeCaptcha(HttpSession session) {
        session.removeAttribute(SESSION_KEY); // 驗證成功才移除
    }

    private String randomCode(int len) {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) sb.append(CHARS.charAt(rnd.nextInt(CHARS.length())));
        return sb.toString();
    }

    private Color randomColor(Random rnd) {
        return new Color(rnd.nextInt(150), rnd.nextInt(150), rnd.nextInt(150));
    }
}
