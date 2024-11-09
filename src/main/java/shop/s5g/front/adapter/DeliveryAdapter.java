package shop.s5g.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.delivery.DeliveryResponseDto;
import shop.s5g.front.dto.delivery.DeliveryUpdateRequestDto;

@FeignClient(name = "delivery", url = "${gateway.url}", path = "/api/shop/delivery", configuration = FeignGatewayAuthorizationConfig.class)
public interface DeliveryAdapter {
    @PutMapping
    DeliveryResponseDto userUpdateDelivery(@RequestBody DeliveryUpdateRequestDto updateRequest);

    @PutMapping("/admin")
    DeliveryResponseDto adminUpdateDelivery(@RequestBody DeliveryUpdateRequestDto updateRequest);


}
