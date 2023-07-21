package pl.twolak.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval;

import pl.twolak.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import pl.twolak.food.ordering.system.order.service.domain.event.OrderPaidEvent;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
