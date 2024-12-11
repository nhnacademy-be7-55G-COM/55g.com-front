package shop.s5g.front.service.coupon.book.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import shop.s5g.front.adapter.coupon.CouponBookAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.book.*;
import shop.s5g.front.dto.coupon.template.CouponTemplateInquiryResponseDto;
import shop.s5g.front.dto.coupon.book.CouponBookRequestDto;
import shop.s5g.front.exception.coupon.CouponBookNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponBookServiceImplTest {

    @Mock
    private CouponBookAdapter couponBookAdapter;

    @InjectMocks
    private CouponBookServiceImpl couponBookService;

    private BookDetailsBookResponseDto bookDetails;
    private CouponBookResponseDto couponBook;
    private CouponTemplateInquiryResponseDto templateInquiryResponseDto;
    private CouponBookDetailsBookTitleResponseDto bookTitleResponseDto;
    private MessageDto messageDto;

    @BeforeEach
    void setUp() {
        // BookDetailsBookResponseDto(
        // long bookId,
        // String publisherName,
        // String typeName,
        // String title,
        // String chapter,
        // String description,
        // LocalDate publishedDate,
        // String isbn,
        // long price,
        // BigDecimal discountRate,
        // boolean isPacked,
        // int stock,
        // long views,
        // LocalDateTime createdAt
        // )
        bookDetails = new BookDetailsBookResponseDto(
            1L,
            "Publisher",
            "Type",
            "Some Title",
            "Chapter 1",
            "Some Description",
            LocalDate.of(2020, 1, 1),
            "1234567890",
            1000L,
            BigDecimal.valueOf(10.5),
            false,
            100,
            2000L,
            LocalDateTime.of(2020, 1, 1, 12, 0)
        );

        // CouponBookResponseDto(
        // Long couponTemplateId,
        // Long bookId,
        // String title,
        // BigDecimal discountPrice,
        // Long condition,
        // Long maxPrice,
        // Integer duration,
        // String couponName,
        // String couponDescription
        // )
        couponBook = new CouponBookResponseDto(
            100L,
            1L,
            "BookTitle",
            BigDecimal.valueOf(200),
            1000L,
            5000L,
            30,
            "SpecialCoupon",
            "Discount on BookTitle"
        );

        // CouponTemplateInquiryResponseDto(
        // Long couponTemplateId,
        // BigDecimal discountPrice,
        // Long condition,
        // Long maxPrice,
        // Integer duration,
        // String couponName,
        // String couponDescription
        // )
        templateInquiryResponseDto = new CouponTemplateInquiryResponseDto(
            100L,
            BigDecimal.valueOf(300),
            1000L,
            5000L,
            15,
            "TemplateCoupon",
            "Some coupon description"
        );

        // CouponBookDetailsBookTitleResponseDto(String title)
        bookTitleResponseDto = new CouponBookDetailsBookTitleResponseDto("Another Book Title");

        messageDto = new MessageDto("Success");
    }

    @Test
    void getBook_Success() {
        when(couponBookAdapter.findBook(1L)).thenReturn(ResponseEntity.ok(bookDetails));

        BookDetailsBookResponseDto result = couponBookService.getBook(1L);
        assertNotNull(result);
        assertEquals("Some Title", result.title());
    }

    @Test
    void getBook_NotFound() {
        when(couponBookAdapter.findBook(1L)).thenThrow(new RuntimeException("Not found"));

        assertThrows(CouponBookNotFoundException.class, () -> couponBookService.getBook(1L));
    }

    @Test
    void getAllBooks_Success() {
        Page<BookDetailsBookResponseDto> page = new PageImpl<>(List.of(bookDetails));
        when(couponBookAdapter.findAllBooks(any())).thenReturn(ResponseEntity.ok(page));

        Page<BookDetailsBookResponseDto> result = couponBookService.getAllBooks(PageRequest.of(0,10));
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Some Title", result.getContent().getFirst().title());
    }

    @Test
    void getAllBooks_NotFound() {
        when(couponBookAdapter.findAllBooks(any())).thenThrow(new RuntimeException("Not found"));

        assertThrows(CouponBookNotFoundException.class, () -> couponBookService.getAllBooks(PageRequest.of(0,10)));
    }

    @Test
    void getAllCouponBooks_Success() {
        Page<CouponBookResponseDto> page = new PageImpl<>(List.of(couponBook));
        when(couponBookAdapter.findAllCouponBooks(any())).thenReturn(ResponseEntity.ok(page));

        Page<CouponBookResponseDto> result = couponBookService.getAllCouponBooks(PageRequest.of(0,10));
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("BookTitle", result.getContent().getFirst().title());
    }

    @Test
    void getAllCouponBooks_NotFound() {
        when(couponBookAdapter.findAllCouponBooks(any())).thenThrow(new RuntimeException("Not found"));

        assertThrows(CouponBookNotFoundException.class, () -> couponBookService.getAllCouponBooks(PageRequest.of(0,10)));
    }

    @Test
    void getAllCouponBooksForCouponTemplate_Success() {
        Page<CouponTemplateInquiryResponseDto> page = new PageImpl<>(List.of(templateInquiryResponseDto));
        when(couponBookAdapter.findAllCouponBooksCouponTemplate(eq(1L), any())).thenReturn(ResponseEntity.ok(page));

        Page<CouponTemplateInquiryResponseDto> result = couponBookService.getAllCouponBooksForCouponTemplate(1L, PageRequest.of(0,10));
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("TemplateCoupon", result.getContent().getFirst().couponName());
    }

    @Test
    void getAllCouponBooksForCouponTemplate_NotFound() {
        when(couponBookAdapter.findAllCouponBooksCouponTemplate(eq(1L), any())).thenThrow(new RuntimeException("Not found"));

        assertThrows(CouponBookNotFoundException.class, () -> couponBookService.getAllCouponBooksForCouponTemplate(1L, PageRequest.of(0,10)));
    }

    @Test
    void getAllCouponBooksForBook_Success() {
        Page<CouponBookDetailsBookTitleResponseDto> page = new PageImpl<>(List.of(bookTitleResponseDto));
        when(couponBookAdapter.findAllCouponBooksTitle(eq(1L), any())).thenReturn(ResponseEntity.ok(page));

        Page<CouponBookDetailsBookTitleResponseDto> result = couponBookService.getAllCouponBooksForBook(1L, PageRequest.of(0,10));
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Another Book Title", result.getContent().getFirst().title());
    }

    @Test
    void getAllCouponBooksForBook_NotFound() {
        when(couponBookAdapter.findAllCouponBooksTitle(eq(1L), any())).thenThrow(new RuntimeException("Not found"));

        assertThrows(CouponBookNotFoundException.class, () -> couponBookService.getAllCouponBooksForBook(1L, PageRequest.of(0,10)));
    }

    @Test
    void createCouponBook_Success() {
        CouponBookRequestDto requestDto = new CouponBookRequestDto(1L, 1L);
        when(couponBookAdapter.createCouponBooks(requestDto)).thenReturn(ResponseEntity.ok(messageDto));

        MessageDto result = couponBookService.createCouponBook(requestDto);
        assertNotNull(result);
        assertEquals("Success", result.message());
    }

    @Test
    void createCouponBook_NotFound() {
        CouponBookRequestDto requestDto = new CouponBookRequestDto(1L, 1L);
        when(couponBookAdapter.createCouponBooks(requestDto)).thenThrow(new RuntimeException("Create Failed"));

        assertThrows(CouponBookNotFoundException.class, () -> couponBookService.createCouponBook(requestDto));
    }

    @Test
    void createCouponBook_Non2xxResponse() {
        // 2xx가 아닐 경우도 예외 발생
        CouponBookRequestDto requestDto = new CouponBookRequestDto(1L,1L);
        when(couponBookAdapter.createCouponBooks(requestDto)).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

        assertThrows(CouponBookNotFoundException.class, () -> couponBookService.createCouponBook(requestDto));
    }
}
