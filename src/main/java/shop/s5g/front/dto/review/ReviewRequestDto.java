package shop.s5g.front.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ReviewRequestDto(
    @NotNull
    Long bookId,

    @NotNull
    Long orderDetailId,

    @Min(1)
    @Max(5)
    int score,

    String content,
    List<String> imagePathList
) {

    public static ReviewRequestDto from(CreateReviewRequestDto createReviewRequestDto,
        List<String> imagePathList) {
        return new ReviewRequestDto(createReviewRequestDto.bookId(),
            createReviewRequestDto.orderDetailId(), createReviewRequestDto.score(),
            createReviewRequestDto.content(), imagePathList);
    }
}
