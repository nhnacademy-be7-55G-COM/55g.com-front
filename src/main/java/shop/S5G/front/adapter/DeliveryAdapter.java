package shop.S5G.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.S5G.front.dto.delivery.DeliveryResponseDto;
import shop.S5G.front.dto.delivery.DeliveryUpdateRequestDto;

@FeignClient(name = "delivery", url = "${gateway.url}", path = "/api/shop/delivery")
public interface DeliveryAdapter {
    @PutMapping
    DeliveryResponseDto userUpdateDelivery(@RequestBody DeliveryUpdateRequestDto updateRequest);

    @PutMapping("/admin")
    DeliveryResponseDto adminUpdateDelivery(@RequestBody DeliveryUpdateRequestDto updateRequest);
}
