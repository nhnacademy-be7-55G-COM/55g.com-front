package shop.s5g.front.dto.wrappingpaper;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record WrappingPaperCreateRequestDto(
    @Size(min = 1)
    @NotNull
    String name,
    @Min(0)
    int price,
    @NotNull
    MultipartFile imageFile
) {

}
