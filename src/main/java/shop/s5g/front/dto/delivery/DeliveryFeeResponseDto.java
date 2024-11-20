package shop.s5g.front.dto.delivery;

import java.io.Serializable;

public record DeliveryFeeResponseDto(
    long id,
    long fee,
    long condition,
    int refundFee,
    String name
) implements Serializable {
}
