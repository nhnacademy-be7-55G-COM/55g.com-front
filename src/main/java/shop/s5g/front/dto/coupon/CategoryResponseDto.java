package shop.s5g.front.dto.coupon;

public record CategoryResponseDto(

    Long categoryId,

    Long parentCategoryId,

    String categoryName,

    boolean active
) {
}
