package shop.s5g.front.service.payments;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.front.adapter.PaymentsAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.service.payments.impl.PaymentsServiceImpl;

@ExtendWith(MockitoExtension.class)
class PaymentsServiceImplTest {
    @Mock
    PaymentsAdapter adapter;

    @InjectMocks
    PaymentsServiceImpl service;

    @Test
    @Disabled   // TODO: RabbitMQ로 전환하여 나중에 다시 재작성
    void confirmPaymentTest() {
        when(adapter.confirmPayment(any())).thenReturn(new MessageDto("good"));

        assertThatCode(() -> service.confirmPayment(HashMap.newHashMap(1)))
            .doesNotThrowAnyException();

        verify(adapter, times(1)).confirmPayment(any());
    }
}
