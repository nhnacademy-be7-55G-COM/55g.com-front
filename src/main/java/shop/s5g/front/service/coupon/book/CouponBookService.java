package shop.s5g.front.service.coupon.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.book.BookDetailsBookResponseDto;
import shop.s5g.front.dto.coupon.book.CouponBookDetailsBookTitleResponseDto;
import shop.s5g.front.dto.coupon.book.CouponBookRequestDto;
import shop.s5g.front.dto.coupon.book.CouponBookResponseDto;
import shop.s5g.front.dto.coupon.template.CouponTemplateInquiryResponseDto;

public interface CouponBookService {

    BookDetailsBookResponseDto getBook(Long bookId);
    Page<BookDetailsBookResponseDto> getAllBooks(Pageable pageable);

    Page<CouponBookResponseDto> getAllCouponBooks(Pageable pageable);

    Page<CouponTemplateInquiryResponseDto> getAllCouponBooksForCouponTemplate(Long bookId, Pageable pageable);

    Page<CouponBookDetailsBookTitleResponseDto> getAllCouponBooksForBook(Long templateId, Pageable pageable);

    MessageDto createCouponBook(CouponBookRequestDto couponBookRequestDto);
}
