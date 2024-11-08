package shop.s5g.front.dto.order;

import java.util.List;
import shop.s5g.front.dto.delivery.DeliveryResponseDto;
import shop.s5g.front.dto.refund.RefundHistoryResponseDto;

public record OrderDetailInfoDto(
    List<OrderDetailWithBookResponseDto> details,
    DeliveryResponseDto delivery,
    List<RefundHistoryResponseDto> refunds
) {}