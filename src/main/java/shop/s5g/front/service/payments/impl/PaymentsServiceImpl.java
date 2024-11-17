package shop.s5g.front.service.payments.impl;

import jakarta.annotation.Nullable;
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
        @Nullable
        MessageDto response = rabbitService.sendPaymentRequest(paymentBody);
        // 처리가 지연되어 타임아웃이 발생했을 시,
        if (response == null) {
            log.warn("[RabbitMQ] payment process was delayed. Check message queue.");
        } else {
            log.debug("[RabbitMQ] Message from consumer: {}", response.message());
        }
        return response;
//        return adapter.confirmPayment(paymentBody);
    }
}
