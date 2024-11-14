package shop.s5g.front.dto.book;

import java.math.BigDecimal;

public record BookSimpleResponseDto(
    long id,
    String title,
    long price,
    BigDecimal discountRate,
    int stock,
    boolean isPacked,
    String status
) {

}
