package shop.s5g.front.dto.coupon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CouponPolicyInquiryResponseDto(

    @Min(1)
    @NotNull
    Long couponPolicyId,

    @NotNull
    BigDecimal discountPrice,

    @NotNull
    Long condition,

    Long maxPrice,

    @Min(1)
    @NotNull
    Integer duration
) {

}
