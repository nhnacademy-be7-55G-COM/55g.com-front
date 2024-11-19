package shop.s5g.front.service.delivery;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import shop.s5g.front.dto.delivery.DeliveryFeeCreateRequestDto;
import shop.s5g.front.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.front.dto.delivery.DeliveryFeeUpdateRequestDto;

public interface DeliveryFeeService {

    List<DeliveryFeeResponseDto> getAllFees();

    CompletableFuture<LinkedList<DeliveryFeeResponseDto>> getAllFeesAsync();

    DeliveryFeeResponseDto updateFee(DeliveryFeeUpdateRequestDto updateRequest);

    DeliveryFeeResponseDto createFee(DeliveryFeeCreateRequestDto createRequest);
}
