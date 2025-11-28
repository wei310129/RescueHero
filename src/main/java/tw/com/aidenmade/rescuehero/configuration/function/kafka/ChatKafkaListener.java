package tw.com.aidenmade.rescuehero.configuration.function.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tw.com.aidenmade.rescuehero.domain.chat.api.request.ChatMessageRequest;
import tw.com.aidenmade.rescuehero.domain.chat.model.ChatMessage;
import tw.com.aidenmade.rescuehero.domain.chat.repository.ChatMessageRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatKafkaListener {

    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @KafkaListener(
            topics = "${chat.kafka.topics.group}",
            groupId = "${chat.kafka.group-id.chat}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenGroup(ChatMessageRequest message) {
        try {
            ChatMessage doc = new ChatMessage();
            doc.setFrom(message.getFrom());
            doc.setTo(message.getTo());
            doc.setContent(message.getContent());
            doc.setTopic("group");
            chatMessageRepository.save(doc);

            simpMessagingTemplate.convertAndSend("/room/group/" + message.getTo(), message);
        } catch (Exception ex) {
            log.error("Failed to process group message", ex);
        }
    }

    @KafkaListener(
            topics = "${chat.kafka.topics.team}",
            groupId = "${chat.kafka.group-id.chat}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenTeam(ChatMessageRequest message) {
        try {
            ChatMessage doc = new ChatMessage();
            doc.setFrom(message.getFrom());
            doc.setTo(message.getTo());
            doc.setContent(message.getContent());
            doc.setTopic("team");
            chatMessageRepository.save(doc);

            simpMessagingTemplate.convertAndSend("/room/team/" + message.getTo(), message);
        } catch (Exception ex) {
            log.error("Failed to process team message", ex);
        }
    }
}
