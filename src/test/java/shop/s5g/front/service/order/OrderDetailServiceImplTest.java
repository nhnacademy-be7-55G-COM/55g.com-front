package shop.s5g.front.service.order;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.front.adapter.OrderAdapter;
import shop.s5g.front.dto.order.OrderDetailInfoDto;
import shop.s5g.front.service.order.impl.OrderDetailServiceImpl;

@ExtendWith(MockitoExtension.class)
class OrderDetailServiceImplTest {
    @Mock
    OrderAdapter adapter;

    @InjectMocks
    OrderDetailServiceImpl service;

    @Test
    void getOrderDetailAllInfosTest() {
        OrderDetailInfoDto mockDto = mock(OrderDetailInfoDto.class);
        when(adapter.fetchOrderDetailInfo(anyLong())).thenReturn(mockDto);

        assertThatCode(() -> service.getOrderDetailAllInfos(1L))
            .doesNotThrowAnyException();

        verify(adapter, times(1)).fetchOrderDetailInfo(anyLong());
    }

}
