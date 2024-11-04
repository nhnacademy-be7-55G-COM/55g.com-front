package shop.S5G.front.service.order;

import java.util.List;
import shop.S5G.front.dto.order.OrderCreateRequestDto;
import shop.S5G.front.dto.order.OrderQueryRequestDto;
import shop.S5G.front.dto.order.OrderWithDetailResponseDto;

public interface OrderService {

    long createOrder(OrderCreateRequestDto createRequest);

    List<OrderWithDetailResponseDto> queryOrdersBetweenDates(OrderQueryRequestDto queryRequest);
}
