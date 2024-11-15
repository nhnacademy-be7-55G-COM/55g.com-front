package shop.s5g.front.dto.delivery;

import java.time.LocalDate;

public record DeliveryUpdateRequestDto(
    long id,
    String address,
    String invoiceNumber,
    LocalDate receivedDate,
    LocalDate shippingDate,
    String receiverName,
    String status
) {

}
