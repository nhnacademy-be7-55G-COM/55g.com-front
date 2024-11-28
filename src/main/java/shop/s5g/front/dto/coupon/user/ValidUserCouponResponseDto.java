package shop.s5g.front.dto.coupon.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record ValidUserCouponResponseDto(

    @NotNull
    Long couponId,

    @NotBlank
    String couponCode,

    @NotNull
    String couponName,

    @NotBlank
    String couponDescription,

    @NotNull
    Long condition,

    @NotNull
    BigDecimal discountPrice,

    Long maxPrice
) {
    
}
