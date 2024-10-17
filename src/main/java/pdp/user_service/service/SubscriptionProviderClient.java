package pdp.user_service.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pdp.user_service.dto.SubscriptionProviderDto;

import java.util.List;

@Service
public class SubscriptionProviderClient {

    public List<SubscriptionProviderDto> findAllSubscriptionProviders() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ParameterizedTypeReference<List<SubscriptionProviderDto>> typeRef =
                new ParameterizedTypeReference<>() {};

        String url = "http://localhost:8081/subscriptions/all-providers";
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                typeRef).getBody();
    }

}
