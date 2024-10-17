package pdp.user_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    public static final String SUBSCRIBE_UTILITY = "subscribe_utility";
    public static final String CANCEL_SUBSCRIPTION = "cancel_subscription";

    @Bean
    public NewTopic subscribeUtilityTopic() {
        return new NewTopic(SUBSCRIBE_UTILITY, 1, (short) 1);
    }

    @Bean
    public NewTopic cancelSubscriptionTopic() {
        return new NewTopic(CANCEL_SUBSCRIPTION, 1, (short) 1);
    }
}
