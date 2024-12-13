package shop.s5g.front.controller.review;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.front.advice.RedirectWithAlertAdvice;
import shop.s5g.front.config.FilterConfig;
import shop.s5g.front.config.SecurityConfig;
import shop.s5g.front.config.TestSecurityConfig;
import shop.s5g.front.config.TestWebConfig;
import shop.s5g.front.config.WebConfig;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.review.ReviewResponseDto;
import shop.s5g.front.service.review.ReviewService;

@WebMvcTest(
    value = ReviewController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        FilterConfig.class, SecurityConfig.class, WebConfig.class}),
    excludeAutoConfiguration = ThymeleafAutoConfiguration.class
)
@Import({TestSecurityConfig.class, TestWebConfig.class, RedirectWithAlertAdvice.class})
@EnableAspectJAutoProxy
@ActiveProfiles("local")
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    private PageResponseDto<ReviewResponseDto> response;

    @BeforeEach
    void setUp() {
        response = new PageResponseDto<>(List.of(
            new ReviewResponseDto(1L, "spring", "aa11", 1L, 5, "good", LocalDateTime.now(),
                List.of())), 1, 10, 1);
    }

    @Test
    void registerReview() throws Exception {
        // when & then
        mockMvc.perform(multipart("/review")
                .with(csrf())
                .param("bookId", "1")
                .param("orderDetailId", "1")
                .param("score", "5")
                .param("content", "good"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/mypage/review-list"));
    }

    @Test
    void getReviewList() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("reviewAt").descending());
        when(reviewService.getReviewList(pageable)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/mypage/review-list")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(view().name("mypage/review-list"))
            .andExpect(model().attributeExists("reviewList", "page", "totalPage", "pageSize",
                "totalElements"))
            .andExpect(model().attribute("reviewList", response.content()))
            .andExpect(model().attribute("page", 0))
            .andExpect(model().attribute("totalPage", response.totalPage()))
            .andExpect(model().attribute("pageSize", response.pageSize()))
            .andExpect(model().attribute("totalElements", response.totalElements()));

        verify(reviewService, times(1)).getReviewList(pageable);
    }

    @Test
    void updateReview() throws Exception {
        // When & Then
        mockMvc.perform(patch("/review")
                .with(csrf())
                .param("reviewId", "1")
                .param("score", "4")
                .param("content", "good~"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/mypage/review-list"));
    }
}