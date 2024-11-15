package shop.s5g.front.service.order.impl;

import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.OrderAdapter;
import shop.s5g.front.dto.order.OrderCreateRequestDto;
import shop.s5g.front.dto.order.OrderCreateResponseDto;
import shop.s5g.front.dto.order.OrderQueryRequestDto;
import shop.s5g.front.dto.order.OrderWithDetailResponseDto;
import shop.s5g.front.exception.order.OrderCreationFailedException;
import shop.s5g.front.service.order.OrderService;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final OrderAdapter orderAdapter;

    private static final OrderCreationFailedException FAIL_EXCEPTION = new OrderCreationFailedException("주문 생성 실패");

    @Override
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto createRequest) {
        OrderCreateResponseDto response = orderAdapter.createNewOrder(createRequest).getBody();
        if (response == null) {
            throw FAIL_EXCEPTION;
        }
        return response;
    }

    @Override
    public List<OrderWithDetailResponseDto> queryOrdersBetweenDates(
        OrderQueryRequestDto queryRequest) {
        return orderAdapter.fetchOrderListsBetweenDates(
            dateTimeFormatter.format(queryRequest.startDate()),
            dateTimeFormatter.format(queryRequest.endDate())
        );
    }

    @Override
    public void deleteOrder(long orderId) {
        orderAdapter.deleteOrder(orderId);
    }
}
