package shop.s5g.front.dto.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//도서목록 페이지에 필요한 도서 정보 Pageable로 보냄
public record BookPageableResponseDto(
        Long bookId,
        Long publisherId,
        Long bookStatusId,
        @NotNull
        String title,
        String chapter,
        String description,
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime publishedDate,
        String isbn,
        Long price,
        @NotNull
        BigDecimal discountRate,
        @NotNull
        boolean isPacked,
        @NotNull
        int stock,
        @Min(0)
        long views,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @NotNull
        LocalDateTime createdAt,
        String imageName
) {
}
