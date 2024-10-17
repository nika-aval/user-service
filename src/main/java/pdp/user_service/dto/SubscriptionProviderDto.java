package pdp.user_service.dto;

import java.math.BigDecimal;

public record SubscriptionProviderDto(Long id, String name, String description, BigDecimal price) {
}