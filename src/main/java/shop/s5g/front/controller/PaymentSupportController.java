package shop.s5g.front.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.front.annotation.SessionRequired;
import shop.s5g.front.domain.purchase.PurchaseSheet;
import shop.s5g.front.dto.CustomerInfoDto;
import shop.s5g.front.dto.member.MemberInfoResponseDto;
import shop.s5g.front.dto.order.OrderCreateRequestDto;
import shop.s5g.front.dto.order.OrderCreateResponseDto;
import shop.s5g.front.dto.order.PurchaseRequestDto;
import shop.s5g.front.dto.order.WrapModifyRequestDo;
import shop.s5g.front.dto.point.PointUseDto;
import shop.s5g.front.service.order.OrderService;
import shop.s5g.front.utils.PaymentUtils;

// 결제에서 사용할 Rest 호출
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/payment/support")
public class PaymentSupportController {
    private final String tossPaymentsClientKey;
    private final OrderService orderService;
    private final PurchaseSheet purchaseSheet;

    /**
     *  실행 순서:
     *     1. /customer 로 사용자 정보를 가져옴.
     *       1-0. 사용자가 쿠폰 등 여러가지 할인정책을 넣음.
     *       1-1. 사용자가 주문버튼을 누름.
     *     2. /toss/clientKey 로 토스 클라이언트 키를 가져옴.
     *     3. /create-order 로 주문을 생성함.
     *     4. /generate-order 로 토스 결제 ID를 생성함.
     *     5. 토스로 넘어감..
     */

    @GetMapping("/generate-order")
    @SessionRequired
    public String generateOrderId(/* User Auth */HttpServletRequest request, @RequestParam long orderId) {
        String value = purchaseSheet.getRandomOrderId();
        purchaseSheet.setOrderId(orderId);

        log.trace("New Order Id Generated: {}", value);
        return value;
    }

    // TODO: 쿠폰 사용 업데이트
    @PutMapping("/coupon")
    public ResponseEntity<HttpStatus> updateCouponUsage() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/customer")
    public CustomerInfoDto sendCustomerInfo() {
        // TODO: Auth에서 사용자 정보를 가져오거나 api로 요청하여 가져오도록 함.
        //  지금은 회원만 가능함.
        MemberInfoResponseDto info = purchaseSheet.getMemberInfo();
        String customerIdStr = String.valueOf(info.customerId());
        String uuid = PaymentUtils.getUUIDFromCustomerId(info.customerId());

        return new CustomerInfoDto(
            customerIdStr, uuid, info.name(), info.email()
        );
    }

    @GetMapping("/toss/clientKey")
    public String getTossClientKey() {
        return tossPaymentsClientKey;
    }

    @PostMapping("/create-order")
    @SessionRequired
    // TODO: OrderCreateResponseDto
    public ResponseEntity<OrderCreateResponseDto> createNewOrder(HttpServletRequest request, @RequestBody PurchaseRequestDto purchase) {
        purchaseSheet.generateOrder();
        OrderCreateRequestDto order = purchaseSheet.createOrderRequest(purchase.delivery());

//        // TODO: 세션에서 쿠폰 사용 여부, 합계 및 netPrice 가져오기.

        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(order));
    }

    @PutMapping("/wrap")
    public ResponseEntity<HttpStatus> modifyWraps(@RequestBody WrapModifyRequestDo wrapModify) {
        log.trace("Modifying wraps...: {}", wrapModify);
        purchaseSheet.changeWrappingPaper(wrapModify);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/order")
    @SessionRequired
    public ResponseEntity<HttpStatus> deleteOrder(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // 주문 세션에서 주문 ID를 가져와서 삭제 요청을 날림.
        orderService.deleteOrder(purchaseSheet.getOrderId());
        session.invalidate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/point")
    @SessionRequired
    public ResponseEntity<HttpStatus> updateUsingPoint(@RequestBody PointUseDto use) {
        try {
            purchaseSheet.updateUsingPoint(use.point());
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }
}
