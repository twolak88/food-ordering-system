package pl.twolak.food.ordering.system.order.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.twolak.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import pl.twolak.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;

@Slf4j
@Component
public class OrderTrackCommandHandler {

    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return null;
    }
}
