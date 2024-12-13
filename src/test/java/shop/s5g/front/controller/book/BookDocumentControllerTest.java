package shop.s5g.front.controller.book;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.front.config.FilterConfig;
import shop.s5g.front.config.SecurityConfig;
import shop.s5g.front.config.TestSecurityConfig;
import shop.s5g.front.config.TestWebConfig;
import shop.s5g.front.config.WebConfig;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.book.BookDocumentResponseDto;
import shop.s5g.front.service.book.BookDocumentService;

@WebMvcTest(
    value = BookDocumentController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        FilterConfig.class, SecurityConfig.class, WebConfig.class}),
    excludeAutoConfiguration = ThymeleafAutoConfiguration.class
)
@Import({TestSecurityConfig.class, TestWebConfig.class})
@ActiveProfiles("local")
class BookDocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookDocumentService bookDocumentService;

    private PageResponseDto<BookDocumentResponseDto> response;

    @BeforeEach
    void setUp() {
        response = new PageResponseDto<>(List.of(
            new BookDocumentResponseDto(1L, "spring", "chapter 1", "good",
                LocalDateTime.of(2023, 10, 22, 14, 22), "1231231231", 15000L,
                BigDecimal.valueOf(0L), false, 10, 0L,
                LocalDateTime.now(), LocalDateTime.now(), new String[]{"한강"},
                new String[]{"프로그래밍"}, "웅진미디어", null, null)), 1, 10, 1);
    }

    @Test
    void searchByKeyword() throws Exception {
        // given
        String keyword = "spring";
        Pageable pageable = PageRequest.of(0, 10);
        when(bookDocumentService.searchByKeyword(keyword, pageable)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/book/list")
                .param("keyword", keyword)
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(10)))
            .andExpect(status().isOk())
            .andExpect(view().name("book/searchBookList"))
            .andExpect(
                model().attributeExists("books", "page", "totalPage", "pageSize", "totalElements",
                    "keyword"))
            .andExpect(model().attribute("books", response.content()))
            .andExpect(model().attribute("page", 0))
            .andExpect(model().attribute("totalPage", response.totalPage()))
            .andExpect(model().attribute("totalElements", response.totalElements()))
            .andExpect(model().attribute("keyword", keyword));

        verify(bookDocumentService, times(1)).searchByKeyword(keyword, pageable);
    }

    @Test
    void searchByCategoryAndKeyword() throws Exception {
        // given
        String categoryName = "프로그래밍";
        String keyword = "spring";
        Pageable pageable = PageRequest.of(0, 10);
        when(bookDocumentService.searchByCategoryAndKeyword(categoryName, keyword,
            pageable)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/book/list/category")
                .param("name", categoryName)
                .param("keyword", keyword)
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(10)))
            .andExpect(status().isOk())
            .andExpect(view().name("book/categorySearchBookList"))
            .andExpect(
                model().attributeExists("categoryName", "books", "page", "totalPage", "pageSize",
                    "totalElements",
                    "keyword"))
            .andExpect(model().attribute("categoryName", categoryName))
            .andExpect(model().attribute("books", response.content()))
            .andExpect(model().attribute("page", 0))
            .andExpect(model().attribute("totalPage", response.totalPage()))
            .andExpect(model().attribute("totalElements", response.totalElements()))
            .andExpect(model().attribute("keyword", keyword));

        verify(bookDocumentService, times(1)).searchByCategoryAndKeyword(categoryName, keyword,
            pageable);
    }
}