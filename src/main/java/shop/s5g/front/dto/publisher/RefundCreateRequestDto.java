package shop.s5g.front.dto.publisher;

import java.util.List;

public record RefundCreateRequestDto(
    long orderDetailId,
    String reason,
    long typeId,
    List<String> images
) {

}
