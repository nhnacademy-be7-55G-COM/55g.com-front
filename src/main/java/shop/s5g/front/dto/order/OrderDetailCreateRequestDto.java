package shop.s5g.front.dto.order;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import java.io.Serializable;

public record OrderDetailCreateRequestDto(
    @Min(1)
    long bookId,
    @Nullable
    Long wrappingPaperId,
    @Min(1)
    int quantity,
    @Min(0)
    long totalPrice,
    @Min(0)
    int accumulationPrice
) implements Serializable {}
