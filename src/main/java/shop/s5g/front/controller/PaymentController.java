package shop.s5g.front.controller;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.s5g.front.service.payments.PaymentsService;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/payment")
@Controller
public class PaymentController {
    private final PaymentsService paymentsService;
    // https://docs.tosspayments.com/sdk/v2/js#%EC%9D%91%EB%8B%B5-21

    // TODO: 메모
    // 쿠폰사용여부, 장바구니 등을 세션에서 관리하고, javascript의 비동기 전달값과 비교하여 검증.

    @GetMapping("/success")
    public String requestSuccess(
        @RequestParam long amount,
        @RequestParam String orderId,
        @RequestParam String paymentKey) {

        // TODO: 장바구니를 체크

        // TODO: 쿠폰사용여부를 체크
        //
        // TODO: 장바구니와 쿠폰사용여부를 체크하여 결제비용을 검증

        // TODO: 값이 다르면 결제를 cancel하는 api를 shop에 요청.

        Map<String, Object> body = new HashMap<>();
        body.put("amount", amount);
        body.put("orderId", orderId);
        body.put("paymentKey", paymentKey);

        paymentsService.confirmPayment(body);
        log.trace("Toss Payment request success");
        return "payments/toss-success";
    }

    @GetMapping("/fail")
    public String requestFail(@RequestParam String code, @RequestParam String message, @RequestParam String orderId) {
        return "payments/toss-fail";
    }
}
