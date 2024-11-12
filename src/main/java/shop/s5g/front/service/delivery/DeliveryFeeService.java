package shop.s5g.front.service.delivery;

import java.util.List;
import shop.s5g.front.dto.delivery.DeliveryFeeCreateRequestDto;
import shop.s5g.front.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.front.dto.delivery.DeliveryFeeUpdateRequestDto;

public interface DeliveryFeeService {

    List<DeliveryFeeResponseDto> getAllFees();

    DeliveryFeeResponseDto updateFee(DeliveryFeeUpdateRequestDto updateRequest);

    DeliveryFeeResponseDto createFee(DeliveryFeeCreateRequestDto createRequest);
}
