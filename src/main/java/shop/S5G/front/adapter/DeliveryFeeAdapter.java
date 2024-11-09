package shop.s5g.front.adapter;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.delivery.DeliveryFeeCreateRequestDto;
import shop.s5g.front.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.front.dto.delivery.DeliveryFeeUpdateRequestDto;

@FeignClient(name = "deliveryFee", url = "${gateway.url}", path = "/api/shop/delivery/fee", configuration = FeignGatewayAuthorizationConfig.class)
public interface DeliveryFeeAdapter {
    @GetMapping
    List<DeliveryFeeResponseDto> fetchDeliveryFees();

    @PutMapping
    DeliveryFeeResponseDto updateDeliveryFee(@RequestBody DeliveryFeeUpdateRequestDto updateRequest);

    @PostMapping
    DeliveryFeeResponseDto createDeliveryFee(@RequestBody DeliveryFeeCreateRequestDto createRequest);
}
