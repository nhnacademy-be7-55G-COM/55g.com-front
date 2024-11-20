package shop.s5g.front.dto.category;

import jakarta.validation.constraints.NotNull;

public record CategoryDetailResponseDto(
        @NotNull
        long categoryId,
        long parentCategoryId,
        @NotNull
        String categoryName
) {
}
