package shop.S5G.front.service.payments.impl;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.S5G.front.adapter.PaymentsAdapter;
import shop.S5G.front.dto.MessageDto;
import shop.S5G.front.service.payments.PaymentsService;

@RequiredArgsConstructor
@Service
public class PaymentsServiceImpl implements PaymentsService {
    private final PaymentsAdapter adapter;

    @Override
    public MessageDto confirmPayment(Map<String, Object> paymentBody) {
        return adapter.confirmPayment(paymentBody);
    }
}
