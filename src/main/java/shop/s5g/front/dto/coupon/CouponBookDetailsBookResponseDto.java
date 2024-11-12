package shop.s5g.front.dto.coupon;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponBookDetailsBookResponseDto(
    Long bookId,
    Long publisherId,
    Long bookStatusId,
    @NotNull
    String title,
    String chapter,
    String description,
    @NotNull
    LocalDateTime publishedDate,
    String isbn,
    Long price,
    @NotNull
    BigDecimal discountRate,
    @NotNull
    boolean isPacked,
    @NotNull
    int stock,
    @NotNull
    LocalDateTime createdAt,
    String imageName
) {
}
