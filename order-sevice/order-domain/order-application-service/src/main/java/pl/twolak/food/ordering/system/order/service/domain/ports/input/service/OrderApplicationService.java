package pl.twolak.food.ordering.system.order.service.domain.ports.input.service;

import jakarta.validation.Valid;
import pl.twolak.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import pl.twolak.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import pl.twolak.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import pl.twolak.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;

public interface OrderApplicationService {

    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);

    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}
