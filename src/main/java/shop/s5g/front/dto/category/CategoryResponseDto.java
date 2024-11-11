package shop.s5g.front.dto.category;

import jakarta.validation.constraints.NotNull;

public record CategoryResponseDto(
        @NotNull
        Long categoryId,
        Long parentCategoryId,
        @NotNull
        String categoryName,
        @NotNull
        boolean active
) {
}