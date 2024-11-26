package shop.s5g.front.dto.coupon.user;

public record UserCouponRabbitResponseDto(
    boolean isMessageProcessed,
    String message
) {

}
