package shop.s5g.front.dto.category;

import jakarta.validation.constraints.NotNull;

public record CategoryResponseDto(
        String parentCategoryName,
        @NotNull
        String categoryName,
        @NotNull
        boolean active
) {
}
