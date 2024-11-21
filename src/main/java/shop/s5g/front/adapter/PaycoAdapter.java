package shop.s5g.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.jwt.TokenResponseDto;

@FeignClient(name = "payco", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface PaycoAdapter {

    @PostMapping("/api/auth/payco")
    ResponseEntity<TokenResponseDto> getToken(@RequestParam(name = "code") String code);

    @PostMapping("/api/auth/payco/link")
    ResponseEntity<MessageDto> linkAccount(@RequestParam(name = "code") String code);

}
