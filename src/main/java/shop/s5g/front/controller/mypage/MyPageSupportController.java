package shop.s5g.front.controller.mypage;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.front.dto.order.OrderDetailCancelRequestDto;
import shop.s5g.front.dto.order.OrderDetailInfoDto;
import shop.s5g.front.dto.order.OrderQueryRequestDto;
import shop.s5g.front.dto.order.OrderWithDetailResponseDto;
import shop.s5g.front.dto.refund.OrderDetailRefundRequestDto;
import shop.s5g.front.dto.refund.RefundHistoryCreateResponseDto;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.service.order.OrderDetailService;
import shop.s5g.front.service.order.OrderService;
import shop.s5g.front.service.refund.RefundService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage/support")
public class MyPageSupportController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final RefundService refundService;

    @GetMapping("/orders")
    public List<OrderWithDetailResponseDto> fetchOrders(OrderQueryRequestDto request) {
        return orderService.queryOrdersBetweenDates(request);
    }

    @GetMapping("/orders/info")
    public OrderDetailInfoDto fetchOrderInfo(@RequestParam long orderId) {
        return orderDetailService.getOrderDetailAllInfos(orderId);
    }

    // TODO: 멱등키를 프론트에서 발급하여 넣어야함.
    @PutMapping("/orders/cancel/{detailId}")
    public ResponseEntity<HttpStatus> cancelOrderDetail(
        @PathVariable long detailId,
        @RequestBody OrderDetailCancelRequestDto cancelRequest
    ) {
        orderDetailService.cancelDetailRequest(detailId, cancelRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/orders/refund")
    public ResponseEntity<RefundHistoryCreateResponseDto> refundOrderDetail(
        @Valid OrderDetailRefundRequestDto refundRequest,
        BindingResult errors
    ) {
        if (errors.hasErrors()) {
            throw new BadRequestException("환불 형식이 잘못되었습니다.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
            refundService.registerRefund(refundRequest)
        );
    }

    @PutMapping("/orders/confirm/{detailId}")
    public ResponseEntity<HttpStatus> changeDetailStatusToConfirm(@PathVariable long detailId) {
        orderDetailService.changeOrderDetailStatus(detailId, "CONFIRM");
        return ResponseEntity.ok().build();
    }
}
