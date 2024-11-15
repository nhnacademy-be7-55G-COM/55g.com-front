package shop.s5g.front.service.coupon.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.BookDetailsBookResponseDto;
import shop.s5g.front.dto.coupon.CouponBookDetailsBookTitleResponseDto;
import shop.s5g.front.dto.coupon.CouponBookRequestDto;
import shop.s5g.front.dto.coupon.CouponBookResponseDto;
import shop.s5g.front.dto.coupon.CouponTemplateInquiryResponseDto;

public interface CouponBookService {

    BookDetailsBookResponseDto getBook(Long bookId);
    Page<BookDetailsBookResponseDto> getAllBooks(Pageable pageable);

    Page<CouponBookResponseDto> getAllCouponBooks(Pageable pageable);

    Page<CouponTemplateInquiryResponseDto> getAllCouponBooksForCouponTemplate(Long bookId, Pageable pageable);

    Page<CouponBookDetailsBookTitleResponseDto> getAllCouponBooksForBook(Long templateId, Pageable pageable);

    MessageDto createCouponBook(CouponBookRequestDto couponBookRequestDto);
}
