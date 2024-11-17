package shop.s5g.front.dto.book;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookDocumentResponseDto(
    Long bookId,
    Long publisherId,
    Long bookStatusId,
    String title,
    String chapter,
    String description,
    LocalDateTime publishedDate,
    String isbn,
    Long price,
    BigDecimal discountRate,
    boolean isPacked,
    int stock,
    Long views,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}
