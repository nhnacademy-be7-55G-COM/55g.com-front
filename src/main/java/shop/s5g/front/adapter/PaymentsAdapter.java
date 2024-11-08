package shop.s5g.front.adapter;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.dto.MessageDto;

@FeignClient(name = "payments", url="${gateway.url}", path = "/api/shop/payment")
public interface PaymentsAdapter {
    @PostMapping("/confirm")
    MessageDto confirmPayment(@RequestBody Map<String, Object> body);
}
