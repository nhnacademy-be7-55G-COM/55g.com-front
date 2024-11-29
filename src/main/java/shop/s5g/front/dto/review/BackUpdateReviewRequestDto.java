package shop.s5g.front.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record BackUpdateReviewRequestDto(
    @NotNull
    Long reviewId,

    @Min(1)
    @Max(5)
    int score,

    String content,
    List<byte[]> imageByteList,
    List<String> extensions
) {

    public static BackUpdateReviewRequestDto from(UpdateReviewRequestDto updateReviewRequestDto,
        List<byte[]> imageByteList, List<String> extensions) {
        return new BackUpdateReviewRequestDto(updateReviewRequestDto.reviewId(),
            updateReviewRequestDto.score(),
            updateReviewRequestDto.content(), imageByteList, extensions);
    }
}