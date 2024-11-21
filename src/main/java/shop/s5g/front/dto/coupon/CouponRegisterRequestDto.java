package shop.s5g.front.dto.coupon;

public record CouponRegisterRequestDto(

    Integer quantity,
    Long couponTemplateId
) {

}
