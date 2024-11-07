package shop.S5G.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "test", url = "${gateway.url}")
public interface TestAdapter {
    @GetMapping("/api/shop/test")
    ResponseEntity<String> test();

    @GetMapping("/api/shop/test/auth")
    ResponseEntity<String> authTest();
}
