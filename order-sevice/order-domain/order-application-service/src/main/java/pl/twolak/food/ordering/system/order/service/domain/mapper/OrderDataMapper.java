package pl.twolak.food.ordering.system.order.service.domain.mapper;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import pl.twolak.food.ordering.system.domain.valueobject.CustomerId;
import pl.twolak.food.ordering.system.domain.valueobject.Money;
import pl.twolak.food.ordering.system.domain.valueobject.ProductId;
import pl.twolak.food.ordering.system.domain.valueobject.RestaurantId;
import pl.twolak.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import pl.twolak.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import pl.twolak.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import pl.twolak.food.ordering.system.order.service.domain.entity.Order;
import pl.twolak.food.ordering.system.order.service.domain.entity.OrderItem;
import pl.twolak.food.ordering.system.order.service.domain.entity.Product;
import pl.twolak.food.ordering.system.order.service.domain.entity.Restaurant;
import pl.twolak.food.ordering.system.order.service.domain.valueobject.StreetAddress;

import java.util.List;
import java.util.UUID;

@Component
public class OrderDataMapper {

    public Restaurant createRestaurant(CreateOrderCommand createOrderCommand) {

        Assert.notNull(createOrderCommand, "createOrderCommand must not be null");

        List<Product> products = createOrderCommand.getItems().stream()
                .map(orderItem -> new Product(new ProductId(orderItem.getProductId())))
                .toList();

        return Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(products)
                .build();
    }

    public Order createOrder(CreateOrderCommand createOrderCommand) {

        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .deliveryAddress(createStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .items(createOrderItems(createOrderCommand.getItems()))
                .build();
    }

    private List<OrderItem> createOrderItems(
            List<pl.twolak.food.ordering.system.order.service.domain.dto.create.OrderItem> items) {

        return items.stream()
                .map(orderItem -> OrderItem.builder()
                        .product(new Product(new ProductId(orderItem.getProductId())))
                        .price(new Money(orderItem.getPrice()))
                        .quantity(orderItem.getQuantity())
                        .subTotal(new Money(orderItem.getSubTotal()))
                        .build())
                .toList();
    }

    private StreetAddress createStreetAddress(OrderAddress address) {

        return new StreetAddress(UUID.randomUUID(),
                address.getStreet(),
                address.getPostalCode(),
                address.getCity());
    }

    public CreateOrderResponse createOrderResponse(Order order) {

        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .build();
    }
}
