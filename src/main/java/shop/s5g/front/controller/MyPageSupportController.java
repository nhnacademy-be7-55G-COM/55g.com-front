package shop.s5g.front.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.front.annotation.MemberAndAdminOnly;
import shop.s5g.front.dto.order.OrderQueryRequestDto;
import shop.s5g.front.dto.order.OrderWithDetailResponseDto;
import shop.s5g.front.service.order.OrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage/support")
@MemberAndAdminOnly
public class MyPageSupportController {
    private final OrderService orderService;

    // TODO: 현재 INACTIVE 인 주문도 같이 나오는중. API 단에서 수정필요.
    @GetMapping("/orders")
    public List<OrderWithDetailResponseDto> fetchOrders(OrderQueryRequestDto request) {
        return orderService.queryOrdersBetweenDates(request);
    }
}
