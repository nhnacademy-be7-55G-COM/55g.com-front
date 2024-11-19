package shop.s5g.front.dto.bookcategory;

import jakarta.validation.constraints.NotNull;

public record BookCategoryResponseDto(
        @NotNull
        Long categoryId,
        @NotNull
        Long bookId
) {

}
