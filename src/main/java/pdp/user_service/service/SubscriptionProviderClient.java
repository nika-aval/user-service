package pdp.user_service.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pdp.user_service.dto.SubscriptionProviderDto;

import java.util.List;

@Service
public class SubscriptionProviderClient {

    private final RestClient restClient;

    public SubscriptionProviderClient() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }

    public List<SubscriptionProviderDto> findAllSubscriptionProviders() {
        return restClient.get()
                .uri("/subscriptions")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

}
