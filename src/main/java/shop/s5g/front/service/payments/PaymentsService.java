package shop.s5g.front.service.payments;

import java.util.Map;
import shop.s5g.front.dto.order.OrderRabbitResponseDto;

public interface PaymentsService {

    OrderRabbitResponseDto confirmPayment(Map<String, Object> paymentBody);
}
