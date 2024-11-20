package shop.s5g.front.dto.book;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record BookAddRequestDto(
    @NotNull
    Long publisherId,
    @NotNull
    Long bookStatusId,
    @NotNull
    String title,
    String chapter,
    String description,
    @NotNull
    String publishedDate,
    @NotNull
    String isbn,
    @NotNull
    Long price,
    @NotNull
    BigDecimal discountRate,
    Boolean isPacked,
    @NotNull
    int stock
) {

}
