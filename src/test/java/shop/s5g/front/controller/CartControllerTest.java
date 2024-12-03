package shop.s5g.front.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import shop.s5g.front.config.FilterConfig;
import shop.s5g.front.config.SecurityConfig;
import shop.s5g.front.config.TestSecurityConfig;
import shop.s5g.front.config.TestWebConfig;
import shop.s5g.front.config.WebConfig;
import shop.s5g.front.service.cart.CartService;

@WebMvcTest(value = CartController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {FilterConfig.class, SecurityConfig.class, WebConfig.class})
)
@Import({TestSecurityConfig.class, TestWebConfig.class})
class CartControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    CartService service;

    @Test
    void cartLoginCheckTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/cart/loginCheck")
            .cookie(new Cookie("accessJwt", "test")))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(print());
    }
}
