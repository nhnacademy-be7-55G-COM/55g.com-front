package shop.s5g.front.service.coupon.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.front.dto.coupon.CouponBookDetailsBookResponseDto;
import shop.s5g.front.dto.coupon.CouponBookDetailsBookTitleResponseDto;
import shop.s5g.front.dto.coupon.CouponBookResponseDto;
import shop.s5g.front.dto.coupon.CouponTemplateInquiryResponseDto;

public interface CouponBookService {

    CouponBookDetailsBookResponseDto getBook(Long bookId);
    Page<CouponBookDetailsBookResponseDto> getAllBooks(Pageable pageable);

    Page<CouponBookResponseDto> getAllCouponBooks(int page, int size);

    Page<CouponTemplateInquiryResponseDto> getAllCouponBooksForCouponTemplate(Long bookId, int page, int size);

    Page<CouponBookDetailsBookTitleResponseDto> getAllCouponBooksForBook(Long templateId, int page, int size);
}
