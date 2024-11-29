package shop.s5g.front.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record BackCreateReviewRequestDto(
    @NotNull
    Long bookId,

    @NotNull
    Long orderDetailId,

    @Min(1)
    @Max(5)
    int score,

    String content,
    List<byte[]> imageByteList,
    List<String> extensions
) {

    public static BackCreateReviewRequestDto from(CreateReviewRequestDto createReviewRequestDto,
        List<byte[]> imageByteList, List<String> extensions) {
        return new BackCreateReviewRequestDto(createReviewRequestDto.bookId(),
            createReviewRequestDto.orderDetailId(), createReviewRequestDto.score(),
            createReviewRequestDto.content(), imageByteList, extensions);
    }
}
