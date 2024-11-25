package shop.s5g.front.service.delivery.impl;

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.delivery.DeliveryFeeAdapter;
import shop.s5g.front.dto.delivery.DeliveryFeeCreateRequestDto;
import shop.s5g.front.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.front.dto.delivery.DeliveryFeeUpdateRequestDto;
import shop.s5g.front.service.delivery.DeliveryFeeService;

@Service
@RequiredArgsConstructor
public class DeliveryFeeServiceImpl implements DeliveryFeeService {
    private final DeliveryFeeAdapter deliveryFeeAdapter;

    @Override
    public LinkedList<DeliveryFeeResponseDto> getAllFees() {
        return deliveryFeeAdapter.fetchDeliveryFees();
    }

    @Override
    @Async("purchaseExecutor")
    public CompletableFuture<LinkedList<DeliveryFeeResponseDto>> getAllFeesAsync() {
        return CompletableFuture.completedFuture(getAllFees());
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
