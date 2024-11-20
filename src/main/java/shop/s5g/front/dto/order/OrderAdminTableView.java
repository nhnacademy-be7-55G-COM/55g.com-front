package shop.s5g.front.dto.order;

import jakarta.annotation.Nullable;
import java.time.LocalDateTime;

public record OrderAdminTableView(
    long orderId,
    long totalPrice,
    @Nullable
    Long payAmount,
    String deliveryStatus,
    LocalDateTime orderedAt,
    boolean active
) {

}
