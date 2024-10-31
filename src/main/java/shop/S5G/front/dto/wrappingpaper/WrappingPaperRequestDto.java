package shop.S5G.front.dto.wrappingpaper;

import jakarta.validation.constraints.NotNull;

public record WrappingPaperRequestDto(
    @NotNull
    String name,
    int price,
    String imageName
) {
}
