package shop.S5G.front.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.S5G.front.dto.CustomerInfoDto;
import shop.S5G.front.dto.delivery.DeliveryCreateRequestDto;
import shop.S5G.front.service.order.OrderService;
import shop.S5G.front.utils.RandomStringProvider;

// 결제에서 사용할 Rest 호출
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/payment/support")
public class PaymentSupportController {
    private final RandomStringProvider randomStringProvider;
    private final String tossPaymentsClientKey;
    private final OrderService orderService;

    @GetMapping("/generate-order")
    public String generateOrderId(/* User Auth */) {
        // TODO: 세션에 OrderId를 업데이트 할것.
        String value = randomStringProvider.nextString();
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
        return new CustomerInfoDto("2nui23f", "홍길동", "example@example.org");
    }

    @GetMapping("/toss/clientKey")
    public String getTossClientKey() {
        return tossPaymentsClientKey;
    }

    @PostMapping("/create-order")
    // TODO: OrderCreateResponseDto
    public ResponseEntity<HttpStatus> createNewOrder(@RequestBody DeliveryCreateRequestDto delivery) {
        // TODO: 장바구니 가져오기
        // TODO: 세션에서 쿠폰 사용 여부, 합계 및 netPrice 가져오기.
//        orderService.createOrder()
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
