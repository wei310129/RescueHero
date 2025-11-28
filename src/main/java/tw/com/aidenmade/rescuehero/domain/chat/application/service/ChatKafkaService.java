package tw.com.aidenmade.rescuehero.domain.chat.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tw.com.aidenmade.rescuehero.domain.chat.api.request.ChatMessageRequest;

@Service
@RequiredArgsConstructor
public class ChatKafkaService {

    private final KafkaTemplate<String, ChatMessageRequest> kafkaTemplate;

    @Value("${chat.kafka.topics.group}")
    private String groupTopic;

    @Value("${chat.kafka.topics.team}")
    private String teamTopic;

    public void sendToGroup(ChatMessageRequest message) {
        kafkaTemplate.send(groupTopic, message.getTo(), message);
    }

    public void sendToTeam(ChatMessageRequest message) {
        kafkaTemplate.send(teamTopic, message.getTo(), message);
    }
}
