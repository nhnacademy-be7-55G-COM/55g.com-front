package shop.S5G.front.dto.category;

import jakarta.validation.constraints.NotNull;

public record CategoryResponseDto(
        String parentCategoryName,
        @NotNull
        String categoryName,
        @NotNull
        boolean active
) {
}
