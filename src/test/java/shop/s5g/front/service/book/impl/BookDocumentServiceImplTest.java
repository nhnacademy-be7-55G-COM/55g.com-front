package shop.s5g.front.service.book.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import shop.s5g.front.adapter.book.BookDocumentAdapter;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.book.BookDocumentResponseDto;

@ExtendWith(MockitoExtension.class)
class BookDocumentServiceImplTest {

    @Mock
    private BookDocumentAdapter bookDocumentAdapter;

    @InjectMocks
    private BookDocumentServiceImpl bookDocumentService;

    private Pageable pageable;
    private PageResponseDto<BookDocumentResponseDto> response;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        response = new PageResponseDto<>(List.of(
            new BookDocumentResponseDto(1L, "spring", "chapter 1", "good",
                LocalDateTime.of(2023, 10, 22, 14, 22), "1231231231", 15000L,
                BigDecimal.valueOf(0L), false, 10, 0L,
                LocalDateTime.now(), LocalDateTime.now(), new String[]{"한강"},
                new String[]{"프로그래밍"}, "웅진미디어", null, null)), 1, 10, 1);
    }

    @Test
    void searchByKeyword() {
        // given
        String keyword = "spring";
        when(bookDocumentAdapter.searchByKeyword(keyword, pageable)).thenReturn(response);

        // when
        PageResponseDto<BookDocumentResponseDto> result = bookDocumentService.searchByKeyword(
            keyword, pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.totalElements());
        verify(bookDocumentAdapter, times(1)).searchByKeyword(keyword, pageable);
    }

    @Test
    void searchByCategoryAndKeyword() {
        // given
        String categoryName = "프로그래밍";
        String keyword = "spring";
        when(bookDocumentAdapter.searchByCategoryAndKeyword(categoryName, keyword,
            pageable)).thenReturn(response);

        // when
        PageResponseDto<BookDocumentResponseDto> result = bookDocumentService.searchByCategoryAndKeyword(
            categoryName, keyword, pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.totalElements());
        verify(bookDocumentAdapter, times(1)).searchByCategoryAndKeyword(categoryName, keyword,
            pageable);
    }
}