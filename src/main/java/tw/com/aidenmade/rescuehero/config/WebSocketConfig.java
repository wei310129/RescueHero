package tw.com.aidenmade.rescuehero.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 註冊 STOMP 端點（前端連線入口）
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // /ws 是前端 WebSocket / SockJS 要連的 URL
        // 注意：應用有 server.servlet.context-path = /api，實際連線路徑會是 /api/ws
        registry.addEndpoint("/ws")
                // 建議於 production 指定實際 domain，例如 https://rescuehero
                .setAllowedOriginPatterns("https://rescuehero", "http://localhost:8081")
                .withSockJS();
    }

    /**
     * 設定簡單的訊息代理（Broker），用於分流那些 prefix 要交由代理直接轉發，哪些是要直接打到內部端點
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 這兩個 prefix 會交給 broker 處理（用來 SUBSCRIBE）
        registry.enableSimpleBroker("/room", "/user");
        // 前端 SEND 目的地 prefix，會被轉給 @MessageMapping
        registry.setApplicationDestinationPrefixes("/app");
    }
}
