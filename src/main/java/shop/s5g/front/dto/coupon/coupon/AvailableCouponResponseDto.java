package shop.s5g.front.dto.coupon.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AvailableCouponResponseDto(
    Long couponId,

    Long couponTemplateId,

    String couponName,

    LocalDateTime createdAt,

    LocalDateTime expiredAt,

    Long condition,

    Long maxPrice,

    BigDecimal discountPrice,

    Integer count
) {

}
