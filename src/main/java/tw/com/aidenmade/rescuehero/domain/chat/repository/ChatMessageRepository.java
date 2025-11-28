package tw.com.aidenmade.rescuehero.domain.chat.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tw.com.aidenmade.rescuehero.domain.chat.model.ChatMessage;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
}
