package tw.com.aidenmade.rescuehero.domain.chat.api.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import tw.com.aidenmade.rescuehero.domain.chat.api.request.ChatMessageRequest;
import tw.com.aidenmade.rescuehero.domain.chat.application.service.ChatKafkaService;

@Hidden
@Tag(name = "聊天室 WebSocket", description = "處理即時聊天 WebSocket 訊息（非 REST API）")
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatKafkaService chatKafkaService;

    /**
     * 任務群組聊天室 - publish to Kafka topic (listener will forward to websocket & persist)
     */
    @MessageMapping("/chat.group")
    public void sendGroup(ChatMessageRequest message) {
        chatKafkaService.sendToGroup(message);
    }

    /**
     * 群組聊天室 - publish to Kafka topic (listener will forward to websocket & persist)
     */
    @MessageMapping("/chat.team")
    public void sendTeam(ChatMessageRequest message) {
        chatKafkaService.sendToTeam(message);
    }
}
