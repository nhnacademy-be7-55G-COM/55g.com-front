package shop.S5G.front.service.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.S5G.front.adapter.DeliveryAdapter;
import shop.S5G.front.dto.delivery.DeliveryResponseDto;
import shop.S5G.front.dto.delivery.DeliveryUpdateRequestDto;
import shop.S5G.front.service.order.DeliveryService;

@RequiredArgsConstructor
@Service
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryAdapter deliveryAdapter;

    @Override
    public DeliveryResponseDto adminUpdateDelivery(DeliveryUpdateRequestDto update) {
        return deliveryAdapter.adminUpdateDelivery(update);
    }

    @Override
    public DeliveryResponseDto userUpdateDelivery(DeliveryUpdateRequestDto update) {
        return deliveryAdapter.userUpdateDelivery(update);
    }

}
