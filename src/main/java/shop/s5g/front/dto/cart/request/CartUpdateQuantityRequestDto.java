package shop.s5g.front.dto.cart.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record CartUpdateQuantityRequestDto(

    @NotNull
    Long bookId,

    @Range(min = -1, max = 1)
    int change
) {

}
