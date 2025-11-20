package tw.com.aidenmade.rescuehero.chat.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tw.com.aidenmade.rescuehero.chat.api.request.ChatMessage;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 任務群組聊天室
     */
    @MessageMapping("/chat.group")
    public void sendGroup(ChatMessage message) {
        messagingTemplate.convertAndSend("/room/group/" + message.getTo(), message);
    }

    /**
     * 群組聊天室
     */
    @MessageMapping("/chat.team")
    public void sendTeam(ChatMessage message) {
        messagingTemplate.convertAndSend("/room/team/" + message.getTo(), message);
    }
}

