package shop.s5g.front.dto.cart.request;

import jakarta.validation.constraints.NotNull;

public record CartBookSelectRequestDto(
    @NotNull
    Long bookId,

    boolean status
){}
