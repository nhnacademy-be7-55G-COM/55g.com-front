package shop.s5g.front.dto.coupon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CouponPolicyInquiryResponseDto(
    @Min(0)
    @NotNull
    BigDecimal discountPrice,

    @Min(0)
    @NotNull
    Long condition,

    @Min(0)
    @NotNull
    Long maxPrice,

    @Min(0)
    @NotNull
    Integer duration
) {

}
