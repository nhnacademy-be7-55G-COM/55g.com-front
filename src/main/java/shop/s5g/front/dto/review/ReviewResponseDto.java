package shop.s5g.front.dto.review;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewResponseDto(
    Long bookId,
    String bookTitle,
    String memberLoginId,
    Long reviewId,
    int score,
    String content,
    LocalDateTime reviewAt,
    List<String> imagePathList
) {

}