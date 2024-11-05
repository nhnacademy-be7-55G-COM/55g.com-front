package shop.S5G.front.dto.category;

import jakarta.validation.constraints.NotNull;

public record CategoryResponseDto(
        Long parentCategory,
        @NotNull
        String categoryName,
        @NotNull
        boolean active
) {
}
