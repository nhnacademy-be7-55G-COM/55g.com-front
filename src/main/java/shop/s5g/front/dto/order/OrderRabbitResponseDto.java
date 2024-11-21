package shop.s5g.front.dto.order;

public record OrderRabbitResponseDto(
    boolean wellOrdered,
    String message
) {

}
