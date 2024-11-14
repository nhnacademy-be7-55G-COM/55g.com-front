package shop.s5g.front.dto.coupon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CouponBookRequestDto(
    @NotNull
    @Min(1)
    Long couponTemplateId,

    @NotNull
    @Min(1)
    Long bookId
) {

}
