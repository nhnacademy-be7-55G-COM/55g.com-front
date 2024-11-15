package shop.s5g.front.dto.delivery;

import java.time.LocalDate;

public record DeliveryResponseDto(
    long id,
    String address,
    LocalDate receivedDate,
    LocalDate shippingDate,
    int fee,
    String invoiceNumber,
    String receiverName,
    String status
) {
}
