package shop.s5g.front.dto.category;


public record CategoryRequestDto(

        String categoryName,
        Long parentCategoryId

) {
}