package shop.s5g.front.dto.order;

public record PurchaseElementDto(
    long bookId,
    Long wrappingPaperId,
    int quantity,
    long totalPrice
) {
    public OrderDetailCreateRequestDto toDetail(double accRate) {
        return new OrderDetailCreateRequestDto(
            bookId, wrappingPaperId, quantity, totalPrice,
            (int) Math.floor(totalPrice * accRate)
        );
    }
}
