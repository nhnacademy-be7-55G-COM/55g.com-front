package shop.s5g.front.dto.category;

public record CategoryResponseDto(
        long categoryId,
        long parentCategoryId,
        String categoryName,
        boolean active
) {
}
