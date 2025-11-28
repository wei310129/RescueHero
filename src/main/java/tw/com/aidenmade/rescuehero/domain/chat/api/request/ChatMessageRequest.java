package tw.com.aidenmade.rescuehero.domain.chat.api.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatMessageRequest {
    private String from;      // 發送者(ID)
    private String to;        // 接收者（或群組/團隊ID）
    private String content;   // 訊息內容
}

