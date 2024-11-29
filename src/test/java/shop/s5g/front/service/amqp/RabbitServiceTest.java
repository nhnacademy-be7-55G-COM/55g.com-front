package shop.s5g.front.service.amqp;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RabbitService.class)
@TestPropertySource(locations = "classpath:application.yml")
class RabbitServiceTest {
    @MockBean
    RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitService service;

    @Test
    void sendPaymentRequestTest() {
        Map<String, Object> map = new HashMap<>();
        assertThatCode(() -> service.sendPaymentRequest(map))
            .doesNotThrowAnyException();

        verify(rabbitTemplate, times(1)).convertSendAndReceiveAsType(anyString(), anyString(), anyMap(), any());
    }

    @Test
    void sendCouponRequestTest() {
        Map<String, Object> map = new HashMap<>();
        assertThatCode(() -> service.sendCouponRequest(map))
            .doesNotThrowAnyException();

        verify(rabbitTemplate, times(1)).convertSendAndReceiveAsType(anyString(), anyString(), anyMap(), any());
    }
}
