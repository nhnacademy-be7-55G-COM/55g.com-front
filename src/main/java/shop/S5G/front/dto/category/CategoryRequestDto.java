package shop.S5G.front.dto.category;

import jakarta.validation.constraints.NotNull;

public record CategoryRequestDto(

        @NotNull
        String categoryName,

        String parentCategoryName,

        @NotNull
        boolean active
) {
}