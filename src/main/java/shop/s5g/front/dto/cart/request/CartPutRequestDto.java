package shop.s5g.front.dto.cart.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartPutRequestDto (
    @NotNull
    Long bookId,

    @NotNull
    @Min(1)
    Integer quantity
){}
