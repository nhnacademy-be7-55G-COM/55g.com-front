package shop.s5g.front.dto.book;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface BookResponseDto {
    Long getBookId();
    Long getPublisherId();

    Long getBookStatusId();

    String getTitle();

    String getChapter();

    String getDescription();

    LocalDate getPublishedDate();

    String getIsbn();

    Long getPrice();

    BigDecimal getDiscountRate();

    boolean isPacked();

    int getStock();

    Long getViews();

    LocalDateTime getCreatedAt();
}
