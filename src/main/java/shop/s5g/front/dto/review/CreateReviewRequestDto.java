package shop.s5g.front.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateReviewRequestDto(
    @NotNull
    Long bookId,

    @NotNull
    Long orderDetailId,

    @Min(1)
    @Max(5)
    int score,

    String content
) {

}
