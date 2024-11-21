package shop.s5g.front.dto.book;

import java.math.BigDecimal;

public record BookAddSendDto(
    Long publisherId,
    Long bookStatusId,
    String title,
    String chapter,
    String description,
    String publishedDate,
    String isbn,
    Long price,
    BigDecimal discountRate,
    boolean isPacked,
    int stock,
    String thumbnailPath
) {
    public static BookAddSendDto of(BookAddRequestDto dto,String filePath){
        return new BookAddSendDto(
            dto.publisherId(),
            dto.bookStatusId(),
            dto.title(),
            dto.chapter(),
            dto.description(),
            dto.publishedDate(),
            dto.isbn(),
            dto.price(),
            dto.discountRate(),
            dto.isPacked()==null?false:dto.isPacked(),
            dto.stock(),
            filePath
        );
    }
}
