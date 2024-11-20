package shop.s5g.front.adapter;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.MessageDto;

@FeignClient(name = "payments", url="${gateway.url}", path = "/api/shop/payment", configuration = FeignGatewayAuthorizationConfig.class)
public interface PaymentsAdapter {
    @Deprecated
    @PostMapping("/confirm")
    MessageDto confirmPayment(@RequestBody Map<String, Object> body);

    @PutMapping("/cancel")
    MessageDto cancelPayment(@RequestBody Map<String, Object> body);
}
