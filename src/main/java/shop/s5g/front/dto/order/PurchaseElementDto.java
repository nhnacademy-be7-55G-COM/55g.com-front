package shop.s5g.front.dto.order;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

public record PurchaseElementDto(
    @Min(1)
    long bookId,
    @Nullable
    @Min(1)
    Long wrappingPaperId,
    @Min(1)
    int quantity,
    @Min(0)
    long totalPrice
) {
    public OrderDetailCreateRequestDto toDetail(BigDecimal accRate) {
        return new OrderDetailCreateRequestDto(
            bookId, wrappingPaperId, quantity, totalPrice,
            BigDecimal.valueOf(totalPrice).multiply(accRate).intValue()
        );
    }
}
