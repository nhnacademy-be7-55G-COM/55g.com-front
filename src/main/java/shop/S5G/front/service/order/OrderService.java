package shop.S5G.front.service.order;

import shop.S5G.front.dto.order.OrderCreateRequestDto;

public interface OrderService {

    long createOrder(OrderCreateRequestDto createRequest);
}
