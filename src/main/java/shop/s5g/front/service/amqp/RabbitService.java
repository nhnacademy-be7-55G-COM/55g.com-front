package shop.s5g.front.service.amqp;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import shop.s5g.front.dto.MessageDto;

@Service
@RequiredArgsConstructor
public class RabbitService {
    private final RabbitTemplate rabbitTemplate;
    private final ParameterizedTypeReference<MessageDto> messageType = new ParameterizedTypeReference<MessageDto>() {};

    @Value("${rabbit.exchange.orders}")
    private String orderExchange;

    @Value("${rabbit.route.orders.payment.toss}")
    private String tossRouteKey;

    public MessageDto sendPaymentRequest(Map<String, Object> body) {
        // TODO: 응답을 받지 못했을 경우 예외처리를 해야함.
        return rabbitTemplate.convertSendAndReceiveAsType(orderExchange, tossRouteKey, body, messageType);
    }
}