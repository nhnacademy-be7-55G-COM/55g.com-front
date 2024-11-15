package shop.s5g.front.dto.order;

import java.math.BigDecimal;

public record PurchaseElementDto(
    long bookId,
    Long wrappingPaperId,
    int quantity,
    long totalPrice
) {
    public OrderDetailCreateRequestDto toDetail(BigDecimal accRate) {
        return new OrderDetailCreateRequestDto(
            bookId, wrappingPaperId, quantity, totalPrice,
            BigDecimal.valueOf(totalPrice).multiply(accRate).intValue()
        );
    }
}
