package shop.s5g.front.dto.coupon.policy;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CouponPolicyUpdateRequestDto(

    @NotNull
    Long couponPolicyId,

    @NotNull
    BigDecimal discountPrice,

    @NotNull
    Long condition,

    Long maxPrice,

    @NotNull
    Integer duration
) {

}
