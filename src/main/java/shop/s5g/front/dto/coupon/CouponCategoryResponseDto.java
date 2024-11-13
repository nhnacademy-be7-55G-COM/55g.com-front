package shop.s5g.front.dto.coupon;

public record CouponCategoryResponseDto(

    Long categoryId,

    Long parentCategoryId,

    String categoryName,

    boolean active
) {
}
