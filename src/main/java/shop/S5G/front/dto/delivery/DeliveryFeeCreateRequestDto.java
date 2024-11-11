package shop.s5g.front.dto.delivery;

import jakarta.validation.constraints.Size;

public record DeliveryFeeCreateRequestDto(
    long fee,
    long condition,
    int refundFee,
    @Size(max = 20)
    String name
) {

}
