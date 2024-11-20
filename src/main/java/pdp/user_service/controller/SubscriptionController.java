package pdp.user_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pdp.user_service.dto.SubscriptionProviderDto;
import pdp.user_service.service.SubscriptionProducerService;
import pdp.user_service.service.SubscriptionProviderClient;

import java.util.List;

@Tag(name = "Subscriptions", description = "APIs for managing subscriptions")
@RestController
@RequiredArgsConstructor
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionProviderClient subscriptionProviderClient;
    private final SubscriptionProducerService subscriptionProducerService;

    @GetMapping
    @Operation(summary = "Get all subscription providers", description = "Retrieves a list of all available subscription providers")
    public List<SubscriptionProviderDto> getAllSubscriptionProviders() {
        return subscriptionProviderClient.findAllSubscriptionProviders();
    }

    @PostMapping
    @Operation(summary = "Subscribe to a provider", description = "Subscribes a subscription for a specified customer and provider")
    public String subscribeProvider(@Parameter(description = "Customer ID") Long customerId,
                                    @Parameter(description = "Subscription Provider ID") Long subscriptionProviderId) {
        return subscriptionProducerService.subscribeUtility(customerId, subscriptionProviderId);
    }

    @DeleteMapping("/{customerId}/{subscriptionProviderId}")
    @Operation(summary = "Cancel a subscription", description = "Cancels a subscription for a specified customer and provider")
    public String cancelSubscription(@PathVariable Long customerId,
                                     @PathVariable Long subscriptionProviderId) {
        return subscriptionProducerService.cancelSubscription(customerId, subscriptionProviderId);
    }

}
