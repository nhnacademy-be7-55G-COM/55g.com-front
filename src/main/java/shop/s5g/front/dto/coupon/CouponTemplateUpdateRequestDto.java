package shop.s5g.front.dto.coupon;

public record CouponTemplateUpdateRequestDto(
    Long couponTemplateId,
    String couponName,
    String couponDescription
) {

}
