package shop.s5g.front.dto.cart.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CartLoginRequestDto (
    @NotNull
    @Valid
    List<CartBookInfoRequestDto> cartBookInfoList
){}
