package shop.s5g.front.service.cart;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import feign.FeignException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import shop.s5g.front.adapter.BookAdapter;
import shop.s5g.front.adapter.CartAdapter;
import shop.s5g.front.dto.cart.request.CartPutRequestDto;
import shop.s5g.front.exception.cart.CartDetailPageException;
import shop.s5g.front.exception.cart.CartPutException;
import shop.s5g.front.service.cart.impl.CartServiceImpl;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    CartAdapter cartAdapter;

    @Mock
    BookAdapter bookAdapter;

    @InjectMocks
    CartServiceImpl cartService;

    @Test
    void putBookTest() {
        CartPutRequestDto putReqDto = mock(CartPutRequestDto.class);
        Map<String, Integer> expectedResponse = Map.of("cartCountChange", 1);

        when(cartAdapter.putBook(putReqDto)).thenReturn(ResponseEntity.ok(expectedResponse));

        int actualResult = cartService.putBook(putReqDto);

        Assertions.assertEquals(1, actualResult);
    }

    @Test
    void putBookExceptionTest() {
        CartPutRequestDto putReqDto = mock(CartPutRequestDto.class);

        when(cartAdapter.putBook(putReqDto)).thenThrow(FeignException.class);

        assertThatThrownBy(() -> cartService.putBook(putReqDto)).isInstanceOf(
            CartPutException.class);
    }

    @Test
    void getCartDetailPageInfoTest() {
        Map<String, Object> expectedResponse = Map.of("books", new ArrayList<>(), "feeInfo",
            new ArrayList<>());

        when(cartAdapter.getCartDetailPageInfo()).thenReturn(ResponseEntity.ok(expectedResponse));

        Map<String, Object> actualResponse = cartService.getCartDetailPageInfo();

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getCartDetailPageInfoExceptionTest() {

        when(cartAdapter.getCartDetailPageInfo()).thenThrow(FeignException.class);


        assertThatThrownBy(() -> cartService.getCartDetailPageInfo()).isInstanceOf(
            CartDetailPageException.class);
    }

    @Test
    void getCartDetailPageInfoWhenGuestTest() {

        Map<String, Object> expectedResponse = Map.of("books", new ArrayList<>(), "feeInfo",
            new ArrayList<>());

        when(cartAdapter.getCartDetailPageInfoWhenGuest(anyString())).thenReturn(
            ResponseEntity.ok(expectedResponse));

        Map<String, Object> actualResponse = cartService.getCartDetailPageInfoWhenGuest(
            anyString());

        Assertions.assertEquals(expectedResponse, actualResponse);

    }

    @Test
    void getCartDetailPageInfoWhenGuestExceptionTest() {

        when(cartAdapter.getCartDetailPageInfoWhenGuest(anyString())).thenThrow(
            FeignException.class);

        assertThatThrownBy(
            () -> cartService.getCartDetailPageInfoWhenGuest(anyString())).isInstanceOf(
            CartDetailPageException.class);
    }
}
