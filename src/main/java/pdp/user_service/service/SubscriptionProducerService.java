package pdp.user_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pdp.user_service.config.KafkaTopicConfig;
import pdp.user_service.dto.CustomerDto;
import pdp.user_service.dto.SubscriptionDto;
import pdp.user_service.dto.SubscriptionProviderDto;

import java.util.List;

@Service
public class SubscriptionProducerService {

    private final CustomerService customerService;
    private final SubscriptionProviderClient subscriptionProviderClient;
    private final KafkaTemplate<Object, Object> kafkaTemplate;


    public SubscriptionProducerService(CustomerService customerService,
                                       SubscriptionProviderClient subscriptionProviderClient,
                                       KafkaTemplate<Object, Object> kafkaTemplate
                                       ) {
        this.kafkaTemplate = kafkaTemplate;
        this.customerService = customerService;
        this.subscriptionProviderClient = subscriptionProviderClient;
    }

    public String subscribeUtility(Long customerId, Long subscriptionProviderId) {
        CustomerDto customer = validateEntitiesAndReturnCustomer(customerId, subscriptionProviderId);

        kafkaTemplate.send(KafkaTopicConfig.SUBSCRIBE_UTILITY, new SubscriptionDto(subscriptionProviderId, customer));

        return "Subscribed Successfully!";
    }

    public String cancelSubscription(Long customerId, Long subscriptionProviderId) {
        CustomerDto customer = validateEntitiesAndReturnCustomer(customerId, subscriptionProviderId);

        kafkaTemplate.send(KafkaTopicConfig.CANCEL_SUBSCRIPTION, new SubscriptionDto(subscriptionProviderId, customer));

        return "Subscription cancelled Successfully!";
    }

    private CustomerDto validateEntitiesAndReturnCustomer(Long customerId, Long subscriptionProviderId) {
        List<Long> providerIds = subscriptionProviderClient.findAllSubscriptionProviders()
                .stream()
                .map(SubscriptionProviderDto::id)
                .toList();

        CustomerDto customer = customerService.getCustomer(customerId);

        if (!providerIds.contains(subscriptionProviderId)) {
            throw new RuntimeException("SubscriptionProvider id " + subscriptionProviderId + " does not exist");
        }
        return customer;
    }

}
