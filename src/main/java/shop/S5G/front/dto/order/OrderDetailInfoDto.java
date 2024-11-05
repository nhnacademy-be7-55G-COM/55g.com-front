package shop.S5G.front.dto.order;

import java.util.List;
import shop.S5G.front.dto.delivery.DeliveryResponseDto;
import shop.S5G.front.dto.refund.RefundHistoryResponseDto;

public record OrderDetailInfoDto(
    List<OrderDetailWithBookResponseDto> details,
    DeliveryResponseDto delivery,
    List<RefundHistoryResponseDto> refunds
) {}