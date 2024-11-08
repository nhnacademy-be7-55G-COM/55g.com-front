package shop.s5g.front.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/payment")
@Controller
public class PaymentController {
    // https://docs.tosspayments.com/sdk/v2/js#%EC%9D%91%EB%8B%B5-21
    @PostMapping("/toss")
    @ResponseBody
    public String requestPaymentByToss(@RequestBody String body /*, Authentication */) {
        log.trace("payment!: {}", body);
        return "Created";
    }

    @GetMapping("/success")
    public String requestSuccess(@RequestParam long amount, @RequestParam String orderId, @RequestParam String paymentKey) {

        log.trace("Toss Payment request success");
        return "payments/toss-success";
    }

    @GetMapping("/fail")
    public String requestFail(@RequestParam String code, @RequestParam String message, @RequestParam String orderId) {
        return "payments/toss-fail";
    }
}
