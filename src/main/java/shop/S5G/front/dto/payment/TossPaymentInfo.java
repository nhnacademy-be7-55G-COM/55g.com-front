package shop.s5g.front.dto.payment;

public record TossPaymentInfo(
    String paymentKey,
    String orderId,
    long amount
) {

}
