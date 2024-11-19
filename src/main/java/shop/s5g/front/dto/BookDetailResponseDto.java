package shop.s5g.front.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import shop.s5g.front.dto.bookcategory.BookCategoryResponseDto;

//@Builder
public record BookDetailResponseDto(
    long bookId,
    String publisherName,
    String typeName,
    String title,
    String chapter,
    String description,
    LocalDateTime publishedDate,
    String isbn,
    long price,
    BigDecimal discountRate,
    boolean isPacked,
    int stock,
    long views,
    LocalDateTime createdAt,
    List<BookAuthorResponseDto> authorList,
    List<BookCategoryResponseDto> categoryList
) {

}
