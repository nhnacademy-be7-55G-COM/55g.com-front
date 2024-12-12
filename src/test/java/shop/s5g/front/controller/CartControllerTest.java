package shop.s5g.front.controller;



import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.front.config.FilterConfig;
import shop.s5g.front.config.SecurityConfig;
import shop.s5g.front.config.TestSecurityConfig;
import shop.s5g.front.config.TestWebConfig;
import shop.s5g.front.config.WebConfig;
import shop.s5g.front.dto.cart.request.CartBookInfoRequestDto;
import shop.s5g.front.dto.cart.request.CartBookSelectRequestDto;
import shop.s5g.front.dto.cart.request.CartLoginRequestDto;
import shop.s5g.front.dto.cart.request.CartPutRequestDto;
import shop.s5g.front.dto.cart.request.CartRemoveBookRequestDto;
import shop.s5g.front.dto.cart.request.CartUpdateQuantityRequestDto;
import shop.s5g.front.exception.cart.CartConvertException;
import shop.s5g.front.exception.cart.CartDetailPageException;
import shop.s5g.front.exception.cart.CartPurchaseException;
import shop.s5g.front.exception.cart.CartPutException;
import shop.s5g.front.service.cart.CartService;

@WebMvcTest(value = CartController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {FilterConfig.class, SecurityConfig.class, WebConfig.class})
)
@Import({TestSecurityConfig.class, TestWebConfig.class})
class CartControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    CartService service;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void cartLoginCheckTrueTest() throws Exception {

        mvc.perform(get("/cart/loginCheck")
                .cookie(new Cookie("accessJwt", "test")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['isLoggedIn']").value(true));

    }

    @Test
    void cartLoginCheckFalseTest() throws Exception {

        mvc.perform(get("/cart/loginCheck"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['isLoggedIn']").value(false));
    }


    @Test
    void convertCartToRedisTest() throws Exception {
        List<CartBookInfoRequestDto> cartBookInfoList = new ArrayList<>();
        CartLoginRequestDto cartLoginRequestDto = new CartLoginRequestDto(cartBookInfoList);
        String requestBody = objectMapper.writeValueAsString(cartLoginRequestDto);

        Map<String, Integer> expectedResponseBody = Map.of("cartCount", 1);

        when(service.convertCartToRedis(cartLoginRequestDto)).thenReturn(expectedResponseBody);

        mvc.perform(post("/cart/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void convertCartToRedisExceptionTest() throws Exception {
        List<CartBookInfoRequestDto> cartBookInfoList = new ArrayList<>();
        CartLoginRequestDto cartLoginRequestDto = new CartLoginRequestDto(cartBookInfoList);
        String requestBody = objectMapper.writeValueAsString(cartLoginRequestDto);

        when(service.convertCartToRedis(cartLoginRequestDto)).thenThrow(CartConvertException.class);

        mvc.perform(post("/cart/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    void convertCartToRedisValidationFailTest() throws Exception {
        List<CartBookInfoRequestDto> cartBookInfoList = null;
        CartLoginRequestDto cartLoginRequestDto = new CartLoginRequestDto(cartBookInfoList);
        String requestBody = objectMapper.writeValueAsString(cartLoginRequestDto);

        Map<String, Integer> expectedResponseBody = Map.of("cartCount", 1);

        when(service.convertCartToRedis(cartLoginRequestDto)).thenReturn(expectedResponseBody);

        mvc.perform(post("/cart/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    void putBookTest() throws Exception {

        CartPutRequestDto cartPutRequestDto = new CartPutRequestDto(1l, 1);
        String requestBody = objectMapper.writeValueAsString(cartPutRequestDto);
        Map<String, Integer> expectedResponseBody = Map.of("cartCountChange", 1);

        when(service.putBook(cartPutRequestDto)).thenReturn(1);

        mvc.perform(post("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void putBookExceptionTest() throws Exception {

        CartPutRequestDto cartPutRequestDto = new CartPutRequestDto(1l, 1);
        String requestBody = objectMapper.writeValueAsString(cartPutRequestDto);

        when(service.putBook(cartPutRequestDto)).thenThrow(CartPutException.class);

        mvc.perform(post("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    void putBookValidationFailTest() throws Exception {
        CartPutRequestDto cartPutRequestDto = new CartPutRequestDto(1l, 0);
        String requestBody = objectMapper.writeValueAsString(cartPutRequestDto);

        mvc.perform(post("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    record CartDetailInfoResponseDto(
        BigDecimal totalPrice,
        long deliveryFee,
        long freeShippingThreshold
    ) {

    }

    @Test
    void cartDetailPageCustomerTest() throws Exception {

        Map<String, Object> cartDetailPageInfo = new HashMap<>();
        cartDetailPageInfo.put("books", new ArrayList<>());
        cartDetailPageInfo.put("feeInfo",
            new CartDetailInfoResponseDto(BigDecimal.valueOf(0l), 3000l, 30000l));

        when(service.getCartDetailPageInfo()).thenReturn(cartDetailPageInfo);

        mvc.perform(get("/cart/detailPage")
                .cookie(new Cookie("accessJwt", "test")))
            .andExpect(status().isOk())
            .andExpect(view().name("cart/cartDetail"));
    }

    @Test
    void cartDetailPageCustomerExceptionTest() throws Exception {
        when(service.getCartDetailPageInfo()).thenThrow(CartDetailPageException.class);

        mvc.perform(get("/cart/detailPage")
                .cookie(new Cookie("accessJwt", "test")))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));

    }

    @Test
    void cartDetailPageGuestTest() throws Exception {
        Map<String, Object> cartDetailPageInfo = new HashMap<>();
        cartDetailPageInfo.put("books", new ArrayList<>());
        cartDetailPageInfo.put("feeInfo",
            new CartDetailInfoResponseDto(BigDecimal.valueOf(0l), 3000l, 30000l));

        when(service.getCartDetailPageInfoWhenGuest("test")).thenReturn(cartDetailPageInfo);

        mvc.perform(get("/cart/detailPage")
                .param("cartBookInfoList", "test"))
            .andExpect(status().isOk())
            .andExpect(view().name("cart/cartDetail"));

    }

    @Test
    void cartDetailPageGuestExceptionTest() throws Exception {

        when(service.getCartDetailPageInfoWhenGuest("test")).thenThrow(
            CartDetailPageException.class);

        mvc.perform(get("/cart/detailPage")
                .param("cartBookInfoList", "test"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));

    }

    @Test
    void updateQuantityTest() throws Exception {
        CartUpdateQuantityRequestDto cartUpdateQuantityRequestDto = new CartUpdateQuantityRequestDto(
            1l, 1);
        String requestBody = objectMapper.writeValueAsString(cartUpdateQuantityRequestDto);
        doNothing().when(service).updateQuantity(cartUpdateQuantityRequestDto);

        mvc.perform(post("/cart/updateQuantity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    void updateQuantityValidationFailTest() throws Exception {
        CartUpdateQuantityRequestDto cartUpdateQuantityRequestDto = new CartUpdateQuantityRequestDto(
            null, 1);
        String requestBody = objectMapper.writeValueAsString(cartUpdateQuantityRequestDto);

        mvc.perform(post("/cart/updateQuantity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateQuantityExceptionTest() throws Exception {
        CartUpdateQuantityRequestDto cartUpdateQuantityRequestDto = new CartUpdateQuantityRequestDto(
            null, 1);
        String requestBody = objectMapper.writeValueAsString(cartUpdateQuantityRequestDto);

        doThrow(CartDetailPageException.class).when(service)
            .updateQuantity(cartUpdateQuantityRequestDto);

        mvc.perform(post("/cart/updateQuantity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    void removeBookTest() throws Exception {
        CartRemoveBookRequestDto cartRemoveBookRequestDto = new CartRemoveBookRequestDto(1l);
        String requestBody = objectMapper.writeValueAsString(cartRemoveBookRequestDto);

        doNothing().when(service).removeBook(cartRemoveBookRequestDto);

        mvc.perform(delete("/cart/removeBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    void removeBookValidationFailTest() throws Exception {
        CartRemoveBookRequestDto cartRemoveBookRequestDto = new CartRemoveBookRequestDto(null);
        String requestBody = objectMapper.writeValueAsString(cartRemoveBookRequestDto);

        mvc.perform(delete("/cart/removeBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    void removeBookExceptionTest() throws Exception {
        CartRemoveBookRequestDto cartRemoveBookRequestDto = new CartRemoveBookRequestDto(1l);
        String requestBody = objectMapper.writeValueAsString(cartRemoveBookRequestDto);

        doThrow(CartDetailPageException.class).when(service).removeBook(cartRemoveBookRequestDto);

        mvc.perform(delete("/cart/removeBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    void changeBookStatusTest() throws Exception {
        CartBookSelectRequestDto cartBookSelectRequestDto = new CartBookSelectRequestDto(1l, true);
        String requestBody = objectMapper.writeValueAsString(cartBookSelectRequestDto);

        doNothing().when(service).changeBookStatusInCart(cartBookSelectRequestDto);

        mvc.perform(post("/cart/changeBookStatus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    void changeBookStatusValidationFailTest() throws Exception {

        CartBookSelectRequestDto cartBookSelectRequestDto = new CartBookSelectRequestDto(null,
            true);
        String requestBody = objectMapper.writeValueAsString(cartBookSelectRequestDto);

        doNothing().when(service).changeBookStatusInCart(cartBookSelectRequestDto);

        mvc.perform(post("/cart/changeBookStatus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
            .andExpect(status().isBadRequest());

    }

    @Test
    void changeBookStatusExceptionTest() throws Exception {

        CartBookSelectRequestDto cartBookSelectRequestDto = new CartBookSelectRequestDto(1l, true);

        String requestBody = objectMapper.writeValueAsString(cartBookSelectRequestDto);

        doThrow(CartDetailPageException.class).when(service)
            .changeBookStatusInCart(cartBookSelectRequestDto);

        mvc.perform(post("/cart/changeBookStatus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
            .andExpect(status().isBadRequest());

    }

    @Test
    void removePurchasedBooksTest() throws Exception {

        doNothing().when(service).removePurchasedBooks();

        mvc.perform(post("/cart/removePurchasedBooks")
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    void removePurchasedBooksExceptionTest() throws Exception {

        doThrow(CartPurchaseException.class).when(service).removePurchasedBooks();

        mvc.perform(post("/cart/removePurchasedBooks")
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }


}
