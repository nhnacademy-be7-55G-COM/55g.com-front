package shop.s5g.front.dto.review;

import java.time.LocalDateTime;

public record ReviewResponseDto(
    Long bookId,
    String bookTitle,
    String memberLoginId,
    Long reviewId,
    int score,
    String content,
    LocalDateTime reviewAt
) {

}