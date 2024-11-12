package shop.s5g.front.dto.cart.request;

import jakarta.validation.constraints.NotNull;

public record CartRemoveBookRequestDto(
    @NotNull
    Long bookId
) {

}
