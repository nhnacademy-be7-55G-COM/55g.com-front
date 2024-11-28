package shop.s5g.front.dto.customer;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GuestOrderQueryForm(
    @NotNull
    @Size(min=4, max=4)
    @Digits(integer = 4, fraction = 0)
    String phoneMiddle,

    @NotNull
    @Size(min=4, max=4)
    @Digits(integer = 4, fraction = 0)
    String phoneEnd,

    @NotNull
    @Size(min=1)
    String name,

    @NotNull
    @Size(min=1)
    String password
) {

}
