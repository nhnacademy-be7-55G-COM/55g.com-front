package shop.s5g.front.service.order;

import java.util.List;
import shop.s5g.front.dto.order.OrderCreateRequestDto;
import shop.s5g.front.dto.order.OrderQueryRequestDto;
import shop.s5g.front.dto.order.OrderWithDetailResponseDto;

public interface OrderService {

    long createOrder(OrderCreateRequestDto createRequest);

    List<OrderWithDetailResponseDto> queryOrdersBetweenDates(OrderQueryRequestDto queryRequest);
}
