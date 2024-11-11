package shop.s5g.front.dto.coupon;

import java.time.LocalDateTime;

public record CouponRegisterRequestDto(

    Long couponTemplateId,
    LocalDateTime expiredAt
) {

}
