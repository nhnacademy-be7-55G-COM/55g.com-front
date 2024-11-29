package shop.s5g.front.dto.coupon.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record ValidUserCouponResponseDto(

    Long couponId,

    String couponCode,

    String couponName,

    String couponDescription,

    Long condition,

    BigDecimal discountPrice,

    Long maxPrice,

    LocalDateTime createdAt,

    LocalDateTime expiredAt
) {

    public String getDay() {
        long days = ChronoUnit.DAYS.between(createdAt, expiredAt);
        return days >= 0 ? "D-" + days : "만료됨";
    }

}
