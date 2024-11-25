package shop.s5g.front.dto.coupon.coupon;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record CouponResponseDto(

    Long couponId,

    Long couponTemplateId,

    String couponCode,

    LocalDateTime createdAt,

    LocalDateTime expiredAt,

    LocalDateTime usedAt,

    String formattedCreatedAt,
    String formattedExpiredAt

) {
    public CouponResponseDto {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");

        formattedCreatedAt = createdAt != null ? createdAt.format(formatter) : null;
        formattedExpiredAt = expiredAt != null ? expiredAt.format(formatter) : null;
    }

}
