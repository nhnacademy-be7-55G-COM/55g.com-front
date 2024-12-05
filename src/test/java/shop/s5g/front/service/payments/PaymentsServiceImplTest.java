package shop.s5g.front.service.payments;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.front.dto.order.OrderRabbitResponseDto;
import shop.s5g.front.service.amqp.RabbitService;
import shop.s5g.front.service.payments.impl.PaymentsServiceImpl;

@ExtendWith(MockitoExtension.class)
class PaymentsServiceImplTest {
    @Mock
    RabbitService rabbitService;

    @InjectMocks
    PaymentsServiceImpl service;

    OrderRabbitResponseDto orderResponse = new OrderRabbitResponseDto(true, "confirmed");

    @Test
    void confirmPaymentTest() {
        when(rabbitService.sendPaymentRequest(anyMap())).thenReturn(orderResponse);

        assertThatCode(() -> service.confirmPayment(HashMap.newHashMap(1)))
            .doesNotThrowAnyException();

        verify(rabbitService, times(1)).sendPaymentRequest(anyMap());
    }

    @Test
    void confirmPaymentDelayedTest() {
        when(rabbitService.sendPaymentRequest(anyMap())).thenReturn(null);

        assertThatCode(() -> service.confirmPayment(HashMap.newHashMap(1)))
            .doesNotThrowAnyException();

        verify(rabbitService, times(1)).sendPaymentRequest(anyMap());
    }
}
