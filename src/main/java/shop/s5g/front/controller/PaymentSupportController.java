package shop.s5g.front.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
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
import shop.s5g.front.domain.PurchaseSheet;
import shop.s5g.front.dto.CustomerInfoDto;
import shop.s5g.front.dto.member.MemberInfoResponseDto;
import shop.s5g.front.dto.order.OrderCreateRequestDto;
import shop.s5g.front.dto.order.OrderCreateResponseDto;
import shop.s5g.front.dto.order.OrderDetailCreateRequestDto;
import shop.s5g.front.dto.order.PurchaseRequestDto;
import shop.s5g.front.service.member.MemberService;
import shop.s5g.front.service.order.OrderService;
import shop.s5g.front.service.point.PointPolicyService;
import shop.s5g.front.utils.PaymentUtils;
import shop.s5g.front.utils.RandomStringProvider;

// 결제에서 사용할 Rest 호출
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/payment/support")
public class PaymentSupportController {
    private final RandomStringProvider randomStringProvider;
    private final String tossPaymentsClientKey;
    private final OrderService orderService;
    private final MemberService memberService;
    private final PointPolicyService pointPolicyService;
    private final PurchaseSheet purchaseSheet;

    @GetMapping("/generate-order")
    @SessionRequired
    public String generateOrderId(/* User Auth */HttpServletRequest request, @RequestParam long orderId) {
        HttpSession session = request.getSession(false);
        String value = randomStringProvider.nextString();
//        session.setAttribute("orderSession", new OrderSession(
//            orderId, value
//        ));
        session.setAttribute("orderDataId", orderId);
        session.setAttribute("paymentOrderId", value);

        log.trace("New Order Id Generated: {}", value);
        return value;
    }

    // TODO: 쿠폰 사용 업데이트
    @PutMapping("/coupon")
    public ResponseEntity<HttpStatus> updateCouponUsage() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/customer")
    public CustomerInfoDto sendCustomerInfo(/* User Auth */) {
        // TODO: Auth에서 사용자 정보를 가져오거나 api로 요청하여 가져오도록 함.
        //  지금은 회원만 가능함.
        MemberInfoResponseDto info = memberService.getMemberInfo();
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
        // TODO: 멤버 정보에서 포인트 적립률과 기본 적립률을 가져오기.
        BigDecimal accRate = pointPolicyService.getPointAccRateForPurchase();
        HttpSession session = request.getSession(false);
        // TODO: 장바구니 가져오기
        // TODO: 세션에서 쿠폰 사용 여부, 합계 및 netPrice 가져오기.
        Long sum = (Long) session.getAttribute("purchase-summation");

        // TODO: 비즈니스 로직 다른데로 옮기기.
        List<OrderDetailCreateRequestDto> cartList = purchase.elements().stream().map(
            p -> p.toDetail(accRate)
        ).toList();
        OrderCreateRequestDto order = new OrderCreateRequestDto(
            purchase.customerId(),
            purchase.delivery(),
            cartList,
            purchase.netPrice(),
            purchase.totalPrice()
        );
        session.setAttribute("order", order);

        if (sum != purchase.totalPrice()) {
            // TODO: 적절한 예외로 바꾸기.
            throw new RuntimeException();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(order));
    }

    @DeleteMapping("/order")
    @SessionRequired
    public ResponseEntity<HttpStatus> deleteOrder(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // 주문 세션에서 주문 ID를 가져와서 삭제 요청을 날림.
        orderService.deleteOrder((long) session.getAttribute("orderDataId"));
        session.invalidate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/test")
    @SessionRequired
    public String test() {
        purchaseSheet.hello();
        return "good";
    }
}
