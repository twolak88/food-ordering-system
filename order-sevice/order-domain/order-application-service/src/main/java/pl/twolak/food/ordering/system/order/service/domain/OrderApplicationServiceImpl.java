package pl.twolak.food.ordering.system.order.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import pl.twolak.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import pl.twolak.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import pl.twolak.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import pl.twolak.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import pl.twolak.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;

@Slf4j
@Validated
@Service
class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderCreateCommandHandler orderCreateCommandHandler;
    private final OrderTrackCommandHandler orderTrackCommandHandler;

    @Autowired
    public OrderApplicationServiceImpl(OrderCreateCommandHandler orderCreateCommandHandler,
                                       OrderTrackCommandHandler orderTrackCommandHandler) {

        Assert.notNull(orderCreateCommandHandler, "orderCreateCommandHandler must not be null");
        Assert.notNull(orderTrackCommandHandler, "orderTrackCommandHandler must not be null");

        this.orderCreateCommandHandler = orderCreateCommandHandler;
        this.orderTrackCommandHandler = orderTrackCommandHandler;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {

        Assert.notNull(createOrderCommand, "createOrderCommand must not be null");

        return orderCreateCommandHandler.createOrder(createOrderCommand);
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {

        Assert.notNull(trackOrderQuery, "trackOrderQuery must not be null");

        return orderTrackCommandHandler.trackOrder(trackOrderQuery);
    }
}
