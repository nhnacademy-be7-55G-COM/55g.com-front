package shop.S5G.front.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/payment")
@RestController
public class PaymentController {
    @PostMapping("/toss")
    public String createPaymentByToss(@RequestBody String body) {
        log.trace("payment!: {}", body);
        return "Created";
    }
}
