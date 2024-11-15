package shop.s5g.front.dto.book;

public record BookPurchaseView(
    long id,
    String title,
    long price,
    int quantity,
    long discountPrice,
    long totalPrice
) {

}
