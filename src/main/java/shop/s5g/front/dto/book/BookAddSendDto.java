package shop.s5g.front.dto.book;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public record BookAddSendDto(
    Long publisherId,
    Long bookStatusId,
    long categoryId,
    String title,
    String chapter,
    String description,
    String publishedDate,
    String isbn,
    Long price,
    BigDecimal discountRate,
    boolean isPacked,
    int stock,
    String thumbnailPath,
    List<Long> tagIdList,
    List<BookAuthorRequestDto> authorList
) {

    public static BookAddSendDto of(BookAddRequestDto dto, String filePath) {
        try {
            return new BookAddSendDto(
                dto.publisherId(),
                dto.bookStatusId(),
                dto.categoryId(),
                dto.title(),
                dto.chapter(),
                dto.description(),
                dto.publishedDate(),
                dto.isbn(),
                dto.price(),
                dto.discountRate(),
                dto.isPacked() == null ? false : dto.isPacked(),
                dto.stock(),
                filePath,
                dto.tagIdList(),
                new ObjectMapper().readValue(dto.authorJson(),
                    new TypeReference<List<BookAuthorRequestDto>>() {
                    })
            );
        } catch (IOException e) {
            throw new RuntimeException();   // TODO: 적절한 예외로 변경 필요
        }
    }
}
