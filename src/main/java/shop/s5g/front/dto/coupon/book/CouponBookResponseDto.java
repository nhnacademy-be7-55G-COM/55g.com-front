package shop.s5g.front.dto.coupon.book;

import java.math.BigDecimal;

public record CouponBookResponseDto(

    Long couponTemplateId,

    Long bookId,

    String title,

    BigDecimal discountPrice,

    Long condition,

    Long maxPrice,

    Integer duration,

    String couponName,

    String couponDescription
) {

}
