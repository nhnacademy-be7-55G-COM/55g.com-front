package shop.S5G.front.dto.delivery;

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
