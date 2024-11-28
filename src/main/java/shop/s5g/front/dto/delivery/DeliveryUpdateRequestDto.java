package shop.s5g.front.dto.delivery;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public record DeliveryUpdateRequestDto(
    @Min(1)
    long id,
    @Size(min=1)
    String address,
    @Nullable
    String invoiceNumber,   // 배송준비중이 아닐때 체크필요.
    @NotNull
    LocalDate receivedDate,
    @Nullable
    LocalDate shippingDate, // 배송준비중이 아닐때 체크필요.
    @Size(min=1)
    @NotNull
    String receiverName,
    @NotNull
    String status
) {
    private static final List<String> validationTarget = List.of("SHIPPING", "DELIVERED");

    // TODO: Enum 기반 Validation으로 바꾸기

    /**
     * 스스로 validation 체크하는 메소드
     * @return returns true if values are valid, otherwise returns false.
     */
    public boolean selfValidation() {
        if (validationTarget.stream().anyMatch(status::equals)) {
            return shippingDate != null && invoiceNumber != null && !invoiceNumber.isEmpty();
        }
        return true;
    }
}
