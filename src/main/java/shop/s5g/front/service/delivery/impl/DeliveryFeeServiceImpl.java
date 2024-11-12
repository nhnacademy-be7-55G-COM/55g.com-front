package shop.s5g.front.service.delivery.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.DeliveryFeeAdapter;
import shop.s5g.front.dto.delivery.DeliveryFeeCreateRequestDto;
import shop.s5g.front.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.front.dto.delivery.DeliveryFeeUpdateRequestDto;
import shop.s5g.front.service.delivery.DeliveryFeeService;

@Service
@RequiredArgsConstructor
public class DeliveryFeeServiceImpl implements DeliveryFeeService {
    private final DeliveryFeeAdapter deliveryFeeAdapter;

    @Override
    public List<DeliveryFeeResponseDto> getAllFees() {
        return deliveryFeeAdapter.fetchDeliveryFees();
    }

    @Override
    public DeliveryFeeResponseDto updateFee(DeliveryFeeUpdateRequestDto updateRequest) {
        return deliveryFeeAdapter.updateDeliveryFee(updateRequest);
    }

    @Override
    public DeliveryFeeResponseDto createFee(DeliveryFeeCreateRequestDto createRequest) {
        return deliveryFeeAdapter.createDeliveryFee(createRequest);
    }

    // delete는 제공되지 않음.
}
