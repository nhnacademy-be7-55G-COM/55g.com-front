package shop.s5g.front.service.payments;

import java.util.Map;
import shop.s5g.front.dto.MessageDto;

public interface PaymentsService {

    MessageDto confirmPayment(Map<String, Object> paymentBody);
}
