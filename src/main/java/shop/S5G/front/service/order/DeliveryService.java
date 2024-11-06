package shop.S5G.front.service.order;

import shop.S5G.front.dto.delivery.DeliveryResponseDto;
import shop.S5G.front.dto.delivery.DeliveryUpdateRequestDto;

public interface DeliveryService {

    DeliveryResponseDto adminUpdateDelivery(DeliveryUpdateRequestDto update);

    DeliveryResponseDto userUpdateDelivery(DeliveryUpdateRequestDto update);
}
