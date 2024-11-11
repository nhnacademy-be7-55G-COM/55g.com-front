package shop.s5g.front.service.delivery.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.DeliveryAdapter;
import shop.s5g.front.dto.delivery.DeliveryResponseDto;
import shop.s5g.front.dto.delivery.DeliveryUpdateRequestDto;
import shop.s5g.front.service.delivery.DeliveryService;

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
