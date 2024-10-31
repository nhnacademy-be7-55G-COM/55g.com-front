package shop.S5G.front.service.payments;

import java.util.Map;
import shop.S5G.front.dto.MessageDto;

public interface PaymentsService {

    MessageDto confirmPayment(long orderRelationId, Map<String, Object> paymentBody);
}
