package shop.s5g.front.dto.coupon.coupon;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record CouponResponseDto(

    Long couponId,

    Long couponTemplateId,

    String couponCode
) {
}
