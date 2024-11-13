package shop.s5g.front.dto.category;

import jakarta.validation.constraints.NotNull;

public record CategoryRequestDto(

        @NotNull
        String categoryName,

        long parentCategoryId,

        @NotNull
        boolean active
) {
}