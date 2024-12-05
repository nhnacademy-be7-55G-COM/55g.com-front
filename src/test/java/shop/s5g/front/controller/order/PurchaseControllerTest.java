package shop.s5g.front.controller.order;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shop.s5g.front.advice.ControllerExceptionResolver;
import shop.s5g.front.advice.RedirectWithAlertAdvice;
import shop.s5g.front.config.FilterConfig;
import shop.s5g.front.config.SecurityConfig;
import shop.s5g.front.config.TestSecurityConfig;
import shop.s5g.front.config.TestWebConfig;
import shop.s5g.front.config.WebConfig;
import shop.s5g.front.domain.purchase.PurchaseSheet;
import shop.s5g.front.dto.cart.request.CartBookInfoRequestDto;
import shop.s5g.front.service.cart.CartService;

@WebMvcTest(value = PurchaseController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        FilterConfig.class, SecurityConfig.class, WebConfig.class}),
    excludeAutoConfiguration = ThymeleafAutoConfiguration.class
)
@Import({TestSecurityConfig.class, TestWebConfig.class, RedirectWithAlertAdvice.class})
@EnableAspectJAutoProxy
@ActiveProfiles("local")
class PurchaseControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    PurchaseSheet purchaseSheet;
    @MockBean
    CartService cartService;
    @SpyBean
    ControllerExceptionResolver exceptionResolver;

    MockHttpSession session = new MockHttpSession();

    @Test
    void purchaseViewTest() throws Exception{
        when(cartService.getBooksWhenPurchase()).thenReturn(List.of(new CartBookInfoRequestDto(1L, 1)));
        when(purchaseSheet.pushCartList(any())).thenReturn(purchaseSheet);

        mvc.perform(MockMvcRequestBuilders.get("/purchase")
                .session(session)
            )
            .andExpect(status().isOk())
            .andDo(print());

        verify(cartService, times(1)).getBooksWhenPurchase();
        // Advice가 작동하지 않고 제대로 model을 리턴했는지 체크.
        verify(exceptionResolver, never()).redirectToAlert(any());
    }

    @Test
    void purchaseEmptyCartTest() throws Exception{
        when(cartService.getBooksWhenPurchase()).thenReturn(List.of());

        mvc.perform(MockMvcRequestBuilders.get("/purchase")
                .session(session)
            )
            .andExpect(model().attribute("redirect", "/"));

        verify(cartService, times(1)).getBooksWhenPurchase();
        verify(purchaseSheet, never()).pushCartList(any());
        verify(exceptionResolver, times(1)).redirectToAlert(any());
    }

    String encodedCart = "W3siYm9va0lkIjoxLCJxdWFudGl0eSI6MX1d";

    String wrongFormatCart = "eyJib29rSWQiOjEsInF1YW50aXR5IjoxfQ==";

    String emptyCart = "W10=";
    @Test
    void purchaseInstantCartTest()throws Exception {
        when(purchaseSheet.pushCartList(any())).thenReturn(purchaseSheet);

        mvc.perform(MockMvcRequestBuilders.get("/purchase/instant")
            .session(session)
            .param("cart", encodedCart)
        ).andExpect(status().isOk());

        verify(purchaseSheet, times(1)).pushToModel(any());
        verify(exceptionResolver, never()).redirectToAlert(any());
    }

    @Test
    void purchaseInstantCartWrongFormatTest() throws Exception {
        when(purchaseSheet.pushCartList(any())).thenReturn(purchaseSheet);

        mvc.perform(MockMvcRequestBuilders.get("/purchase/instant")
            .session(session)
            .param("cart", wrongFormatCart)
        )
            .andExpect(status().isOk())
            .andExpect(model().attribute("redirect", "/"))
            .andExpect(model().attribute("message", containsString("형식")));

        verify(exceptionResolver, times(1)).redirectToAlert(any());
        verify(purchaseSheet, never()).pushToModel(any());
    }

    @Test
    void purchaseInstantCartEmptyTest() throws Exception {
        when(purchaseSheet.pushCartList(any())).thenReturn(purchaseSheet);

        mvc.perform(MockMvcRequestBuilders.get("/purchase/instant")
            .session(session)
            .param("cart", emptyCart))
            .andExpect(model().attribute("redirect", "/"))
            .andExpect(model().attribute("message", containsString("비어")));

        verify(exceptionResolver, times(1)).redirectToAlert(any());
        verify(purchaseSheet, never()).pushToModel(any());
    }
}
