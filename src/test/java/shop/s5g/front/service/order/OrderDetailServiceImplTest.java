package shop.s5g.front.service.order;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
import shop.s5g.front.adapter.PaymentsAdapter;
import shop.s5g.front.dto.order.OrderDetailCancelRequestDto;
import shop.s5g.front.dto.order.OrderDetailInfoDto;
import shop.s5g.front.service.order.impl.OrderDetailServiceImpl;

@ExtendWith(MockitoExtension.class)
class OrderDetailServiceImplTest {
    @Mock
    OrderAdapter orderAdapter;
    @Mock
    PaymentsAdapter paymentsAdapter;

    @InjectMocks
    OrderDetailServiceImpl service;

    @Test
    void getOrderDetailAllInfosTest() {
        OrderDetailInfoDto mockDto = mock(OrderDetailInfoDto.class);
        when(orderAdapter.fetchOrderDetailInfo(anyLong())).thenReturn(mockDto);

        assertThatCode(() -> service.getOrderDetailAllInfos(1L))
            .doesNotThrowAnyException();

        verify(orderAdapter, times(1)).fetchOrderDetailInfo(anyLong());
    }

    @Test
    void changeOrderDetailStatusTest() {
        assertThatCode(() -> service.changeOrderDetailStatus(1L, "CONFIRM"))
            .doesNotThrowAnyException();

        verify(orderAdapter, times(1)).changeDetailType(eq(1L), any());
    }

    OrderDetailCancelRequestDto cancel = new OrderDetailCancelRequestDto("some reasons");

    @Test
    void cancelDetailRequestTest() {
        assertThatCode(() -> service.cancelDetailRequest(1L, cancel))
            .doesNotThrowAnyException();

        verify(paymentsAdapter, times(1)).cancelPayment(anyMap());
    }

    @Test
    void getOrderDetailAllInfosByUUIDTest() {
        assertThatCode(() -> service.getOrderDetailAllInfos("UUID"))
            .doesNotThrowAnyException();

        verify(orderAdapter, times(1)).fetchOrderDetailInfo(anyString());
    }
}
