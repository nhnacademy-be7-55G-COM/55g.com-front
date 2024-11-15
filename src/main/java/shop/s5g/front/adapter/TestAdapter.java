package shop.s5g.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;

@FeignClient(value = "test", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface TestAdapter {
    @GetMapping("/api/shop/test")
    ResponseEntity<String> test();

    @GetMapping("/api/shop/test/auth")
    ResponseEntity<String> authTest();
}
