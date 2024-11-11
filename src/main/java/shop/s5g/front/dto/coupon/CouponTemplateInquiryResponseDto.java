package shop.s5g.front.dto.coupon;

import java.math.BigDecimal;

public record CouponTemplateInquiryResponseDto(

    Long couponPolicyId,

    BigDecimal discountPrice,

    Long condition,

    Long maxPrice,

    Integer duration,

    String couponName,

    String couponDescription
) {

}
