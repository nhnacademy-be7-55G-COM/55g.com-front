package shop.S5G.front.service.payments.impl;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.S5G.front.adapter.PaymentsAdapter;
import shop.S5G.front.dto.MessageDto;

@RequiredArgsConstructor
@Service
public class PaymentsServiceImpl implements shop.S5G.front.service.payments.PaymentsService {
    private final PaymentsAdapter adapter;

    @Override
    public MessageDto confirmPayment(long orderRelationId, Map<String, Object> paymentBody) {
        return adapter.confirmPayment(orderRelationId, paymentBody);
    }
}
