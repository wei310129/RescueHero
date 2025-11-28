// Create Kafka configuration with producer and consumer factories for ChatMessage
package tw.com.aidenmade.rescuehero.configuration.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${chat.kafka.topics.group}")
    private String groupTopicName;

    @Value("${chat.kafka.topics.team}")
    private String teamTopicName;

    @Bean
    public NewTopic groupChatTopic() {
        return buildTopic(groupTopicName, 5, 1);
    }

    @Bean
    public NewTopic teamChatTopic() {
        return buildTopic(teamTopicName, 5, 1);
    }

    private NewTopic buildTopic(String topicName, int partitions, int replicas) {
        return TopicBuilder.name(topicName)
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }
}
