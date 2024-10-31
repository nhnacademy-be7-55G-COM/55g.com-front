package shop.S5G.front.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import shop.S5G.front.dto.delivery.DeliveryCreateRequestDto;

public record OrderCreateRequestDto(
    @Min(1)
    long customerId,
    @NotNull
    DeliveryCreateRequestDto delivery,
    @Size(min = 1)
    List<OrderDetailCreateRequestDto> cartList,
    @Min(0)
    long netPrice,
    @Min(0)
    long totalPrice
) {}
