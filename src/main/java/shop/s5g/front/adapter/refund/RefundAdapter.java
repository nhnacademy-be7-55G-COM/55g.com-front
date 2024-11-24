package shop.s5g.front.adapter.refund;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.publisher.RefundCreateRequestDto;
import shop.s5g.front.dto.refund.RefundHistoryCreateResponseDto;

@FeignClient(name="refund", url="${gateway.url}", path="/api/shop/refund", configuration = FeignGatewayAuthorizationConfig.class)
public interface RefundAdapter {
    @PostMapping
    RefundHistoryCreateResponseDto createRefund(@RequestBody RefundCreateRequestDto createRequest);
}
