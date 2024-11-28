package shop.s5g.front.service.refund;

import shop.s5g.front.dto.refund.OrderDetailRefundRequestDto;
import shop.s5g.front.dto.refund.RefundHistoryCreateResponseDto;

public interface RefundService {

    RefundHistoryCreateResponseDto registerRefund(OrderDetailRefundRequestDto request);
}
