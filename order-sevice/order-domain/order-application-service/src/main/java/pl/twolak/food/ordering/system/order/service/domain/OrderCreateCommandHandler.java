package pl.twolak.food.ordering.system.order.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import pl.twolak.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import pl.twolak.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import pl.twolak.food.ordering.system.order.service.domain.entity.Customer;
import pl.twolak.food.ordering.system.order.service.domain.entity.Order;
import pl.twolak.food.ordering.system.order.service.domain.entity.Restaurant;
import pl.twolak.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import pl.twolak.food.ordering.system.order.service.domain.exception.OrderDomainException;
import pl.twolak.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import pl.twolak.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import pl.twolak.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import pl.twolak.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    @Autowired
    public OrderCreateCommandHandler(OrderDomainService orderDomainService,
                                     OrderRepository orderRepository,
                                     CustomerRepository customerRepository,
                                     RestaurantRepository restaurantRepository,
                                     OrderDataMapper orderDataMapper) {

        Assert.notNull(orderDomainService, "orderDomainService must not be null");
        Assert.notNull(orderRepository, "orderRepository must not be null");
        Assert.notNull(customerRepository, "customerRepository must not be null");
        Assert.notNull(restaurantRepository, "restaurantRepository must not be null");
        Assert.notNull(orderDataMapper, "orderDataMapper must not be null");


        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {

        Assert.notNull(createOrderCommand, "createOrderCommand must not be null");

        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);

        Order order = orderDataMapper.createOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);

        Order saveOrderResult = saveOrder(order);
        log.info("Order is created with id: {}", saveOrderResult.getId().getValue());

        return orderDataMapper.createOrderResponse(saveOrderResult);
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {

        Restaurant restaurant = orderDataMapper.createRestaurant(createOrderCommand);
        Optional<Restaurant> foundRestaurant = restaurantRepository.findRestaurantInformation(restaurant);


        return foundRestaurant
                .orElseThrow(() -> {

                    log.warn("Could not find restaurant with restaurant id: {}", createOrderCommand.getRestaurantId());
                    return new OrderDomainException("Could not find restaurant with restaurant id: " + createOrderCommand.getRestaurantId());
                });
    }

    private void checkCustomer(UUID customerId) {

        Optional<Customer> customer = customerRepository.findCustomer(customerId);

        if (customer.isEmpty()) {

            log.warn("Could not find customer with customer id: {}", customerId);
            throw new OrderDomainException("Could not find customer with customer id: " + customerId);
        }
    }

    private Order saveOrder(Order order) {

        Order saveOrderResult = orderRepository.save(order);

        if (saveOrderResult == null) {

            log.warn("Could not save order.");
            throw new OrderDomainException("Could not save order.");
        }

        log.info("Order is saved with id: {}", saveOrderResult.getId().getValue());

        return saveOrderResult;
    }
}
