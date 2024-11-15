package shop.s5g.front.service.order;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import shop.s5g.front.adapter.OrderAdapter;
import shop.s5g.front.dto.order.OrderCreateRequestDto;
import shop.s5g.front.dto.order.OrderCreateResponseDto;
import shop.s5g.front.dto.order.OrderQueryRequestDto;
import shop.s5g.front.exception.order.OrderCreationFailedException;
import shop.s5g.front.service.order.impl.OrderServiceImpl;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    OrderAdapter adapter;

    @InjectMocks
    OrderServiceImpl service;

    OrderCreateRequestDto mockRequest = mock(OrderCreateRequestDto.class);
    OrderCreateResponseDto mockResponse = mock(OrderCreateResponseDto.class);

    @Test
    void createOrderTest() {
        ResponseEntity<OrderCreateResponseDto> resp = ResponseEntity.ok(mockResponse);
        when(adapter.createNewOrder(any())).thenReturn(resp);

        assertThatCode(() -> service.createOrder(mockRequest))
            .doesNotThrowAnyException();

        verify(adapter, times(1)).createNewOrder(any());
    }

    @Test
    void createOrderFailTest() {
        ResponseEntity<OrderCreateResponseDto> resp = ResponseEntity.ok(null);
        when(adapter.createNewOrder(any())).thenReturn(resp);

        assertThatThrownBy(() -> service.createOrder(mockRequest))
            .isInstanceOf((OrderCreationFailedException.class));

        verify(adapter, times(1)).createNewOrder(any());
    }

    OrderQueryRequestDto query = new OrderQueryRequestDto(
        LocalDate.of(2022, 2, 2), LocalDate.of(2022, 2, 2)
    );

    @Test
    void queryOrdersBetweenDatesTest() {
        assertThatCode(() -> service.queryOrdersBetweenDates(query))
            .doesNotThrowAnyException();

        verify(adapter, times(1))
            .fetchOrderListsBetweenDates(eq("2022-02-02"), eq("2022-02-02"));
    }

    @ParameterizedTest
    @MethodSource("shop.s5g.front.service.order.OrderServiceImplTest#argumentProvider")
    void deleteOrderTest(long l) {
        assertThatCode(() -> service.deleteOrder(l))
            .doesNotThrowAnyException();

        verify(adapter, times(1)).deleteOrder(l);
    }

    // 한번 parameterized test 해봤음..
    static Stream<? extends Arguments> argumentProvider() {
        Stream.Builder<Arguments> args = Stream.builder();
        for (int i=0; i<10; i++) {
            args.add(Arguments.of(i));
        }
        return args.build();
    }

}
