package shop.s5g.front.dto.category;

public record CategoryOneResponseDto(
        long categoryId,
        String categoryName,
        boolean active
) {
}
