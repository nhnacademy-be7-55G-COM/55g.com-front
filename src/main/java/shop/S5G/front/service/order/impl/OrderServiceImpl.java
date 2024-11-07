package shop.S5G.front.service.order.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.S5G.front.adapter.OrderAdapter;
import shop.S5G.front.dto.order.OrderCreateRequestDto;
import shop.S5G.front.dto.order.OrderCreateResponseDto;
import shop.S5G.front.dto.order.OrderQueryRequestDto;
import shop.S5G.front.dto.order.OrderWithDetailResponseDto;
import shop.S5G.front.exception.order.OrderCreationFailedException;
import shop.S5G.front.service.order.OrderService;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderAdapter orderAdapter;

    private static final OrderCreationFailedException FAIL_EXCEPTION = new OrderCreationFailedException("주문 생성 실패");

    @Override
    public long createOrder(OrderCreateRequestDto createRequest) {
        OrderCreateResponseDto response = orderAdapter.createNewOrder(createRequest).getBody();
        if (response == null) {
            throw FAIL_EXCEPTION;
        }
        return response.orderId();
    }

    @Override
    public List<OrderWithDetailResponseDto> queryOrdersBetweenDates(
        OrderQueryRequestDto queryRequest) {
        return orderAdapter.fetchOrderListsBetweenDates(queryRequest);
    }
}
