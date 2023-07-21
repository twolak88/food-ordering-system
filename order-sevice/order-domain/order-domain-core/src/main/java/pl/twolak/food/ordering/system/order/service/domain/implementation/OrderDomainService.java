package pl.twolak.food.ordering.system.order.service.domain.implementation;

import lombok.extern.slf4j.Slf4j;
import pl.twolak.food.ordering.system.order.service.domain.entity.Order;
import pl.twolak.food.ordering.system.order.service.domain.entity.Product;
import pl.twolak.food.ordering.system.order.service.domain.entity.Restaurant;
import pl.twolak.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import pl.twolak.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import pl.twolak.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import pl.twolak.food.ordering.system.order.service.domain.exception.OrderDomainException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainService implements pl.twolak.food.ordering.system.order.service.domain.OrderDomainService {

    private static final String UTC = "UTC";

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();

        log.info("Order with id: {} is initiated", order.getId().getValue());

        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {

        order.pay();
        log.info("Order with id: {} is paid", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approveOrder(Order order) {

        order.approve();
        log.info("Order with id: {} is approved", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {

        order.initCancel(failureMessages);
        log.info("Order payment is cancelling for order id: {}", order.getId().getValue());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {

        order.cancel(failureMessages);
        log.info("Order with id: {} is cancelled", order.getId().getValue());
    }

    private void validateRestaurant(Restaurant restaurant) {

        if (!restaurant.isActive()) {

            throw new OrderDomainException("Restaurant with id " + restaurant.getId().getValue() +
                    " is currently not active");
        }
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {

        order.getItems().forEach(orderItem -> restaurant.getProducts().forEach(product -> {
            Product currentProduct = orderItem.getProduct();
            if (currentProduct.equals(product)) {
                currentProduct.updateWithConfirmedNameAndPrice(product.getName(), product.getPrice());
            }
        }));
    }
}
