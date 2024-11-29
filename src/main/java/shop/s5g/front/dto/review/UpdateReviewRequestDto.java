package shop.s5g.front.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record UpdateReviewRequestDto(
    @NotNull
    Long reviewId,

    @Min(1)
    @Max(5)
    int score,

    String content,
    List<MultipartFile> attachment
) {

    public List<MultipartFile> validFiles() {
        return attachment == null ? List.of() : attachment.stream()
            .filter(file -> file.getSize() > 0)
            .toList();
    }
}
