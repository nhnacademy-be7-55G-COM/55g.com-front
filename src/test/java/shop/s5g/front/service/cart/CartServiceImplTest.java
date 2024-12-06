package shop.s5g.front.service.cart;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThatCode;
import feign.FeignException;
import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import shop.s5g.front.adapter.BookAdapter;
import shop.s5g.front.adapter.CartAdapter;
import shop.s5g.front.dto.book.BookPurchaseView;
import shop.s5g.front.dto.book.BookSimpleResponseDto;
import shop.s5g.front.dto.cart.request.CartBookInfoRequestDto;
import shop.s5g.front.dto.cart.request.CartBookSelectRequestDto;
import shop.s5g.front.dto.cart.request.CartLoginRequestDto;
import shop.s5g.front.dto.cart.request.CartPutRequestDto;
import shop.s5g.front.dto.cart.request.CartRemoveBookRequestDto;
import shop.s5g.front.dto.cart.request.CartUpdateQuantityRequestDto;
import shop.s5g.front.exception.ResourceNotFoundException;
import shop.s5g.front.exception.cart.CartConvertException;
import shop.s5g.front.exception.cart.CartDetailPageException;
import shop.s5g.front.exception.cart.CartPurchaseException;
import shop.s5g.front.exception.cart.CartPutException;
import shop.s5g.front.exception.cart.CartRemoveAccountException;
import shop.s5g.front.service.book.BookService;
import shop.s5g.front.service.cart.impl.CartServiceImpl;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    CartAdapter cartAdapter;

    @Mock
    BookAdapter bookAdapter;

    @Mock
    BookService bookService;

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

    @Test
    void convertCartToRedisTest() {
        CartLoginRequestDto cartLoginReq = mock(CartLoginRequestDto.class);
        Map<String, Integer> expectedResult = Map.of("cartCount", 1);

        when(cartAdapter.convertCartToRedis(cartLoginReq)).thenReturn(
            ResponseEntity.ok(expectedResult));

        Map<String, Integer> actualResult = cartService.convertCartToRedis(cartLoginReq);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void convertCartToRedisExceptionTest() {
        CartLoginRequestDto cartLoginReq = mock(CartLoginRequestDto.class);

        when(cartAdapter.convertCartToRedis(cartLoginReq)).thenThrow(FeignException.class);

        assertThatThrownBy(() -> cartService.convertCartToRedis(cartLoginReq)).isInstanceOf(
            CartConvertException.class);

    }

    @Test
    void redisToDbWhenLogoutTest() {

        when(cartAdapter.redisToDbWhenLogout()).thenReturn(ResponseEntity.ok().build());

        assertThatCode(() -> cartService.redisToDbWhenLogout()).doesNotThrowAnyException();
    }

    @Test
    void redisToDbWhenLogoutExceptionTest() {
        when(cartAdapter.redisToDbWhenLogout()).thenThrow(FeignException.class);

        assertThatThrownBy(() -> cartService.redisToDbWhenLogout()).isInstanceOf(
            CartConvertException.class);
    }

    @Test
    void removeAccountTest() {
        when(cartAdapter.removeAccount()).thenReturn(ResponseEntity.ok().build());

        assertThatCode(() -> cartService.removeAccount()).doesNotThrowAnyException();
    }

    @Test
    void removeAccountExceptionTest() {
        when(cartAdapter.removeAccount()).thenThrow(FeignException.class);

        assertThatThrownBy(() -> cartService.removeAccount()).isInstanceOf(
            CartRemoveAccountException.class);
    }

    @Test
    void updateQuantityTest() {
        CartUpdateQuantityRequestDto updateQuantityReqDto = mock(
            CartUpdateQuantityRequestDto.class);

        when(cartAdapter.updateQuantity(updateQuantityReqDto)).thenReturn(
            ResponseEntity.ok().build());

        assertThatCode(
            () -> cartService.updateQuantity(updateQuantityReqDto)).doesNotThrowAnyException();

    }

    @Test
    void updateQuantityExceptionTest() {
        CartUpdateQuantityRequestDto updateQuantityReqDto = mock(
            CartUpdateQuantityRequestDto.class);

        when(cartAdapter.updateQuantity(updateQuantityReqDto)).thenThrow(FeignException.class);

        assertThatThrownBy(() -> cartService.updateQuantity(updateQuantityReqDto)).isInstanceOf(
            CartDetailPageException.class);

    }

    @Test
    void removeBookTest() {
        CartRemoveBookRequestDto cartRemoveBookReqDto = mock(CartRemoveBookRequestDto.class);

        when(cartAdapter.removeBook(cartRemoveBookReqDto)).thenReturn(ResponseEntity.ok().build());

        assertThatCode(
            () -> cartService.removeBook(cartRemoveBookReqDto)).doesNotThrowAnyException();
    }

    @Test
    void removeBookExceptionTest() {
        CartRemoveBookRequestDto cartRemoveBookReqDto = mock(CartRemoveBookRequestDto.class);

        when(cartAdapter.removeBook(cartRemoveBookReqDto)).thenThrow(FeignException.class);

        assertThatThrownBy(() -> cartService.removeBook(cartRemoveBookReqDto)).isInstanceOf(
            CartDetailPageException.class);
    }

    @Test
    void getBooksWhenPurchase() {
        CartBookInfoRequestDto cartBookInfoRequestDto = mock(CartBookInfoRequestDto.class);
        List<CartBookInfoRequestDto> expectedList = new ArrayList<>();
        expectedList.add(cartBookInfoRequestDto);

        when(cartAdapter.getBooksWhenPurchase()).thenReturn(ResponseEntity.ok(expectedList));

        List<CartBookInfoRequestDto> actualList = cartService.getBooksWhenPurchase();

        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    void getBooksWhenPurchaseExceptionTest() {

        when(cartAdapter.getBooksWhenPurchase()).thenThrow(FeignException.class);

        assertThatThrownBy(() -> cartService.getBooksWhenPurchase()).isInstanceOf(
            CartPurchaseException.class);
    }

    @Test
    void removePurchasedBooksTest() {

        when(cartAdapter.removePurchasedBooks()).thenReturn(ResponseEntity.ok().build());

        assertThatCode(() -> cartService.removePurchasedBooks()).doesNotThrowAnyException();
    }

    @Test
    void removePurchasedBooksExceptionTest() {

        when(cartAdapter.removePurchasedBooks()).thenThrow(FeignException.class);

        assertThatThrownBy(() -> cartService.removePurchasedBooks()).isInstanceOf(
            CartPurchaseException.class);
    }

    @Test
    void changeBookStatusInCartTest() {
        CartBookSelectRequestDto cartBookSelectRequestDto = mock(CartBookSelectRequestDto.class);

        when(cartAdapter.changeBookStatusInCart(cartBookSelectRequestDto)).thenReturn(
            ResponseEntity.ok().build());

        assertThatCode(() -> cartService.changeBookStatusInCart(
            cartBookSelectRequestDto)).doesNotThrowAnyException();
    }

    @Test
    void changeBookStatusInCartExceptionTest() {
        CartBookSelectRequestDto cartBookSelectRequestDto = mock(CartBookSelectRequestDto.class);

        when(cartAdapter.changeBookStatusInCart(cartBookSelectRequestDto)).thenThrow(
            FeignException.class);

        assertThatThrownBy(() -> cartService.changeBookStatusInCart(cartBookSelectRequestDto));

    }

    @Test
    void convertCartToViewTest() {
        List<CartBookInfoRequestDto> cartList = new ArrayList<>();
        cartList.add(new CartBookInfoRequestDto(1l, 1));
        List<BookSimpleResponseDto> bookList = new ArrayList<>();
        bookList.add(
            new BookSimpleResponseDto(1l, "title1", 10000l, BigDecimal.valueOf(0.1), 100, true,
                "status1"));

        List<BookPurchaseView> bookView = new ArrayList<>(cartList.size());
        bookView.add(new BookPurchaseView(1l, "title1", 10000l, 1, 1000l, 9000l));

        when(bookService.getSimpleBooksFromCart(cartList)).thenReturn(bookList);

        CompletableFuture<List<BookPurchaseView>> actualResult = cartService.convertCartToView(
            cartList);

        Assertions.assertEquals(bookView, actualResult.join());

    }

    @Test
    void convertCartToViewExceptionTest() {
        List<CartBookInfoRequestDto> cartList = new ArrayList<>();
        cartList.add(new CartBookInfoRequestDto(1l, 1));
        List<BookSimpleResponseDto> bookList = new ArrayList<>();

        when(bookService.getSimpleBooksFromCart(cartList)).thenReturn(bookList);

        assertThatThrownBy(() -> cartService.convertCartToView(cartList)).isInstanceOf(
            ResourceNotFoundException.class);

    }

}
