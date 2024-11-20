package pdp.user_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import pdp.user_service.config.KafkaTopicConfig;
import pdp.user_service.dto.CustomerDto;
import pdp.user_service.dto.SubscriptionDto;
import pdp.user_service.dto.SubscriptionProviderDto;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionProducerServiceTest {

    @Mock
    private CustomerService customerService;
    @Mock
    private SubscriptionProviderClient subscriptionProviderClient;
    @Mock
    private KafkaTemplate<Object, Object> kafkaTemplate;
    @InjectMocks
    private SubscriptionProducerService subscriptionProducerService;

    @Test
    void shouldSubscribeUtilitySuccessfully() {
        // GIVEN
        Long customerId = 1L;
        Long subscriptionProviderId = 100L;

        CustomerDto customerDto = generateCustomerDto(customerId);
        SubscriptionDto expectedSubscriptionDto = new SubscriptionDto(subscriptionProviderId, customerDto);

        when(customerService.getCustomer(customerId)).thenReturn(customerDto);
        when(subscriptionProviderClient.findAllSubscriptionProviders())
                .thenReturn(generateSubscriptionProviders(subscriptionProviderId));

        // WHEN
        String result = subscriptionProducerService.subscribeUtility(customerId, subscriptionProviderId);

        // THEN
        assertThat(result).isEqualTo("Subscribed Successfully!");

        verify(kafkaTemplate).send(KafkaTopicConfig.SUBSCRIBE_UTILITY, expectedSubscriptionDto);
        verify(customerService).getCustomer(customerId);
        verify(subscriptionProviderClient).findAllSubscriptionProviders();
    }

    @Test
    void shouldThrowExceptionWhenSubscriptionProviderDoesNotExist() {
        // GIVEN
        Long customerId = 1L;
        Long subscriptionProviderId = 100L;

        CustomerDto customerDto = generateCustomerDto(customerId);
        List<SubscriptionProviderDto> providerDtos = generateSubscriptionProviders(subscriptionProviderId);

        when(customerService.getCustomer(customerId)).thenReturn(customerDto);
        when(subscriptionProviderClient.findAllSubscriptionProviders()).thenReturn(providerDtos);

        // WHEN && THEN
        assertThatThrownBy(() -> subscriptionProducerService.subscribeUtility(customerId, 99L))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessageContaining("SubscriptionProvider id 99 does not exist");
        verify(customerService).getCustomer(customerId);
        verify(subscriptionProviderClient).findAllSubscriptionProviders();
        verifyNoInteractions(kafkaTemplate);
    }

    @Test
    void shouldCancelSubscriptionSuccessfully() {
        // GIVEN
        Long customerId = 1L;
        Long subscriptionProviderId = 100L;

        CustomerDto customerDto = generateCustomerDto(customerId);
        SubscriptionDto expectedSubscriptionDto = new SubscriptionDto(subscriptionProviderId, customerDto);
        List<SubscriptionProviderDto> providerDtos = generateSubscriptionProviders(subscriptionProviderId);

        when(customerService.getCustomer(customerId)).thenReturn(customerDto);
        when(subscriptionProviderClient.findAllSubscriptionProviders()).thenReturn(providerDtos);

        // WHEN
        String result = subscriptionProducerService.cancelSubscription(customerId, subscriptionProviderId);

        // THEN
        assertThat(result).isEqualTo("Subscription cancelled Successfully!");

        verify(kafkaTemplate).send(KafkaTopicConfig.CANCEL_SUBSCRIPTION, expectedSubscriptionDto);
        verify(customerService).getCustomer(customerId);
        verify(subscriptionProviderClient).findAllSubscriptionProviders();
    }

    @Test
    void shouldThrowExceptionWhenSubscriptionProviderDoesNotExistOnCancel() {
        // GIVEN
        Long customerId = 1L;
        Long subscriptionProviderId = 100L;

        CustomerDto customerDto = generateCustomerDto(customerId);
        List<SubscriptionProviderDto> providerDtos = generateSubscriptionProviders(subscriptionProviderId);

        when(customerService.getCustomer(customerId)).thenReturn(customerDto);
        when(subscriptionProviderClient.findAllSubscriptionProviders()).thenReturn(providerDtos);

        // WHEN & THEN
        assertThatThrownBy(() -> subscriptionProducerService.cancelSubscription(customerId, 99L))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessageContaining("SubscriptionProvider id 99 does not exist");
        verify(customerService).getCustomer(customerId);
        verify(subscriptionProviderClient).findAllSubscriptionProviders();
        verifyNoInteractions(kafkaTemplate);
    }

    private CustomerDto generateCustomerDto(Long customerId) {
        return new CustomerDto(customerId,
                "Nika",
                "Avalishvili",
                "myemail@gmail.com",
                "+995-555-000-111");
    }

    private List<SubscriptionProviderDto> generateSubscriptionProviders(Long subscriptionProviderId) {
        return List.of(new SubscriptionProviderDto(subscriptionProviderId, "Netflix",
                        "Streaming service that offers a wide variety of TV shows, movies, and much more.",
                        BigDecimal.valueOf(9.99)));
    }

}
