package shop.s5g.front.service.amqp;

import jakarta.annotation.Nullable;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import shop.s5g.front.dto.coupon.user.UserCouponRabbitResponseDto;
import shop.s5g.front.dto.order.OrderRabbitResponseDto;

@Service
@RequiredArgsConstructor
public class RabbitService {
    private final RabbitTemplate rabbitTemplate;
    private final ParameterizedTypeReference<OrderRabbitResponseDto> messageType = new ParameterizedTypeReference<OrderRabbitResponseDto>() {};
    private final ParameterizedTypeReference<UserCouponRabbitResponseDto> userCouponMessageType = new ParameterizedTypeReference<UserCouponRabbitResponseDto>() {};

    @Value("${rabbit.exchange.orders}")
    private String orderExchange;

    @Value("${rabbit.exchange.coupon}")
    private String couponExchange;

    @Value("${rabbit.route.orders.payment.toss}")
    private String tossRouteKey;

    @Value("${rabbit.route.coupon.create}")
    private String couponRouteKey;

    @Nullable
    public OrderRabbitResponseDto sendPaymentRequest(Map<String, Object> body) {
        // 타임아웃이 발생되어 응답을 받지 못했을 경우 null.
        return rabbitTemplate.convertSendAndReceiveAsType(orderExchange, tossRouteKey, body, messageType);
    }

    @Nullable
    public UserCouponRabbitResponseDto sendCouponRequest(Map<String, Object> couponId) {
        return rabbitTemplate.convertSendAndReceiveAsType(couponExchange, couponRouteKey, couponId, userCouponMessageType);
    }

}