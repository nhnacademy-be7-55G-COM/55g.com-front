package shop.s5g.front.dto.category;

import jakarta.validation.constraints.NotNull;

public record CategoryResponseDto(
        long categoryId,
        long parentCategoryId,
        String categoryName,
        boolean active
) {
}
