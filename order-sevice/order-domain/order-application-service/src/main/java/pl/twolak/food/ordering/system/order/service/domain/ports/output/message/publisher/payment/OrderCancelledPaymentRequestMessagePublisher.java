package pl.twolak.food.ordering.system.order.service.domain.ports.output.message.publisher.payment;

import pl.twolak.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import pl.twolak.food.ordering.system.order.service.domain.event.OrderCancelledEvent;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {
}
