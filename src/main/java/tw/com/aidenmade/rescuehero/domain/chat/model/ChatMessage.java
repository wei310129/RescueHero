package tw.com.aidenmade.rescuehero.domain.chat.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "chat_messages")
public class ChatMessage {
    @Id
    private String id;

    private String from;
    private String to;
    private String content;
    private String topic; // which kafka topic
    private Instant createdAt = Instant.now();
}
