package shop.s5g.front.dto.delivery;

public record DeliveryFeeResponseDto(
    long id,
    long fee,
    long condition,
    int refundFee,
    String name
) {
}
