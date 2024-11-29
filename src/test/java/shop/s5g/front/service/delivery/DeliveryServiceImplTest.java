package shop.s5g.front.service.delivery;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.front.adapter.delivery.DeliveryAdapter;
import shop.s5g.front.dto.delivery.DeliveryUpdateRequestDto;
import shop.s5g.front.service.delivery.impl.DeliveryServiceImpl;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceImplTest {
    @Mock
    DeliveryAdapter deliveryAdapter;

    @InjectMocks
    DeliveryServiceImpl service;

    DeliveryUpdateRequestDto update = mock(DeliveryUpdateRequestDto.class);

    @Test
    void adminUpdateDeliveryTest() {
        assertThatCode(() -> service.adminUpdateDelivery(update)).doesNotThrowAnyException();

        verify(deliveryAdapter, times(1)).adminUpdateDelivery(any());
    }

    @Test
    void userUpdateDeliveryTest() {
        assertThatCode(() -> service.userUpdateDelivery(update)).doesNotThrowAnyException();

        verify(deliveryAdapter, times(1)).userUpdateDelivery(any());
    }
}
