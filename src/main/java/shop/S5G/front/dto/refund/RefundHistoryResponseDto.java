package shop.S5G.front.dto.refund;

import java.time.LocalDateTime;

public record RefundHistoryResponseDto(
    long orderDetailId,
    String bookTitle,
    int quantity,
    String type,
    String reason,
    LocalDateTime refundedAt
) {

}