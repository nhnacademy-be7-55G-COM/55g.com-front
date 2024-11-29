package shop.s5g.front.service.delivery;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.front.adapter.delivery.DeliveryFeeAdapter;
import shop.s5g.front.service.delivery.impl.DeliveryFeeServiceImpl;

@ExtendWith(MockitoExtension.class)
class DeliveryFeeServiceImplTest {
    @Mock
    DeliveryFeeAdapter deliveryFeeAdapter;

    @InjectMocks
    DeliveryFeeServiceImpl service;

    @Test
    void getAllFeesTest() {
        assertThatCode(() -> service.getAllFees())
            .doesNotThrowAnyException();

        verify(deliveryFeeAdapter, times(1)).fetchDeliveryFees();
    }

    @Test
    void getAllFeesAsyncTest() {
        assertThatCode(() -> service.getAllFeesAsync())
            .doesNotThrowAnyException();

        verify(deliveryFeeAdapter, times(1)).fetchDeliveryFees();
    }

}
