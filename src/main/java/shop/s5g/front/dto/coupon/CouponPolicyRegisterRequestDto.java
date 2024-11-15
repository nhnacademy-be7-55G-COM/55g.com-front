package shop.s5g.front.dto.coupon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CouponPolicyRegisterRequestDto(
    @Min(0)
    @NotNull
    BigDecimal discountPrice,

    @NotNull
    Long condition,

    Long maxPrice,

    @NotNull
    Integer duration
) {

}
