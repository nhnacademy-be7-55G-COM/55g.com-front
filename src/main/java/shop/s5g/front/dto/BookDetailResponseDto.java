package shop.s5g.front.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

//@Builder
public record BookDetailResponseDto(
    long bookId,
    String publisherName,
    String typeName,
    String title,
    String chapter,
    String description,
    LocalDate publishedDate,
    String isbn,
    long price,
    BigDecimal discountRate,
    boolean isPacked,
    int stock,
    long views,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String imagePath,
    List<BookAuthorResponseDto> authorList,
    List<BookCategoryResponseDto> categoryList
) {

}
