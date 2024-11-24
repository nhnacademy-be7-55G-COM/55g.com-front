package shop.s5g.front.dto.coupon.category;

import java.math.BigDecimal;

public record CouponCategoryResponseDto(

    Long categoryId,

    String categoryName,

    BigDecimal discountPrice,

    Long condition,

    Long maxPrice,

    Integer duration,

    String couponName,

    String couponDescription
) {

}
