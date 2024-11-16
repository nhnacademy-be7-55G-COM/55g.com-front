package shop.s5g.front.service.payments.impl;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.PaymentsAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.service.amqp.RabbitService;
import shop.s5g.front.service.payments.PaymentsService;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentsServiceImpl implements PaymentsService {
    private final PaymentsAdapter adapter;
    private final RabbitService rabbitService;

    @Override
    public MessageDto confirmPayment(Map<String, Object> paymentBody) {
        MessageDto response = rabbitService.sendPaymentRequest(paymentBody);
        log.debug("Rabbit Message from consumer: {}", response.message());
        return response;
//        return adapter.confirmPayment(paymentBody);
    }
}
