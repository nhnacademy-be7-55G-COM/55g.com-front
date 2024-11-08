package shop.s5g.front.dto.wrappingpaper;

import jakarta.validation.constraints.NotNull;

public record WrappingPaperRequestDto(
    @NotNull
    String name,
    int price,
    String imageName
) {
}
