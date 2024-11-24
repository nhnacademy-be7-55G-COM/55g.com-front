package shop.s5g.front.dto.coupon.category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CouponCategoryRequestDto(

    @NotNull
    @Min(1)
    Long categoryId,

    @NotNull
    @Min(1)
    Long couponTemplateId
) {

}
