package shop.s5g.front.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.front.annotation.MemberAndAdminOnly;
import shop.s5g.front.dto.order.OrderDetailInfoDto;
import shop.s5g.front.dto.order.OrderQueryRequestDto;
import shop.s5g.front.dto.order.OrderWithDetailResponseDto;
import shop.s5g.front.service.order.OrderDetailService;
import shop.s5g.front.service.order.OrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage/support")
@MemberAndAdminOnly
public class MyPageSupportController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    @GetMapping("/orders")
    public List<OrderWithDetailResponseDto> fetchOrders(OrderQueryRequestDto request) {
        return orderService.queryOrdersBetweenDates(request);
    }

    @GetMapping("/orders/info")
    public OrderDetailInfoDto fetchOrderInfo(@RequestParam long orderId) {
        return orderDetailService.getOrderDetailAllInfos(orderId);
    }

    @DeleteMapping("/orders/cancel/{detailId}")
    public ResponseEntity<HttpStatus> cancelOrderDetail(@PathVariable long detailId) {

        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/orders/refund/{detailId}")
    public ResponseEntity<HttpStatus> refundOrderDetail(@PathVariable long detailId) {
        return ResponseEntity.ok().build();
    }
}
