package shop.s5g.front.dto.bookcategory;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import shop.s5g.front.dto.BookAuthorResponseDto;
import shop.s5g.front.dto.book.BookDetailCategoryResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record BookCategoryBookDto(
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
        List<BookDetailCategoryResponseDto> categoryList
) {
}
