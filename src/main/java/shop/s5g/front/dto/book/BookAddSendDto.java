package shop.s5g.front.dto.book;

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
    List<Long> tagIdList
) {
    public static BookAddSendDto of(BookAddRequestDto dto,String filePath){
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
            dto.isPacked()==null?false:dto.isPacked(),
            dto.stock(),
            filePath,
            dto.tagIdList()
        );
    }
}
