package shop.s5g.front.dto.coupon.template;

public record CouponTemplateUpdateRequestDto(
    Long couponTemplateId,
    String couponName,
    String couponDescription
) {

}
