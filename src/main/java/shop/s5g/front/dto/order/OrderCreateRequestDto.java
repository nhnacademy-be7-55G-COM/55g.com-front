package shop.s5g.front.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import shop.s5g.front.dto.delivery.DeliveryCreateRequestDto;

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
) implements Serializable {}
