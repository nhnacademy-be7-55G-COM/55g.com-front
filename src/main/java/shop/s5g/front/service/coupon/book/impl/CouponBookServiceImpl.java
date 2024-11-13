package shop.s5g.front.service.coupon.book.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.coupon.CouponBookAdapter;
import shop.s5g.front.dto.coupon.BookDetailsBookResponseDto;
import shop.s5g.front.dto.coupon.CouponBookDetailsBookTitleResponseDto;
import shop.s5g.front.dto.coupon.CouponBookResponseDto;
import shop.s5g.front.dto.coupon.CouponTemplateInquiryResponseDto;
import shop.s5g.front.exception.coupon.CouponBookNotFoundException;
import shop.s5g.front.service.coupon.book.CouponBookService;

@Service
@RequiredArgsConstructor
public class CouponBookServiceImpl implements CouponBookService {

    private final CouponBookAdapter couponBookAdapter;

    @Override
    public BookDetailsBookResponseDto getBook(Long bookId) {
        try {
            ResponseEntity<BookDetailsBookResponseDto> response = couponBookAdapter.findBook(bookId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponBookNotFoundException("CouponBook Not Found...!");
        } catch (RuntimeException e) {
            throw new CouponBookNotFoundException(e.getMessage());
        }
    }

    /**
     * 모든 책 조회
     * @param pageable
     * @return Page<CouponBookDetailsBookResponseDto>
     */
    @Override
    public Page<BookDetailsBookResponseDto> getAllBooks(Pageable pageable) {

        try {
            ResponseEntity<Page<BookDetailsBookResponseDto>> response = couponBookAdapter.findAllBooks(pageable);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponBookNotFoundException("CouponBook Not Found...!");
        } catch (RuntimeException e) {
            throw new CouponBookNotFoundException(e.getMessage());
        }
    }

    /**
     * 쿠폰이 적용된 모든 책 정보 조회
     * @param page
     * @param size
     * @return Page<CouponBookResponseDto>
     */
    @Override
    public Page<CouponBookResponseDto> getAllCouponBooks(int page, int size) {
        try {
            ResponseEntity<Page<CouponBookResponseDto>> response = couponBookAdapter.findAllCouponBooks(page, size);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponBookNotFoundException("CouponBook Not Found...!");
        } catch (RuntimeException e) {
            throw new CouponBookNotFoundException(e.getMessage());
        }
    }

    /**
     * 책에 적용된 모든 쿠폰 템플릿 조회
     * @param bookId
     * @param page
     * @param size
     * @return Page<CouponTemplateInquiryResponseDto>
     */
    @Override
    public Page<CouponTemplateInquiryResponseDto> getAllCouponBooksForCouponTemplate(Long bookId,
        int page, int size) {

        try {
            ResponseEntity<Page<CouponTemplateInquiryResponseDto>> response = couponBookAdapter.findAllCouponBooksCouponTemplate(bookId, page, size);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponBookNotFoundException("CouponBook Not Found...!");
        } catch (RuntimeException e) {
            throw new CouponBookNotFoundException(e.getMessage());
        }
    }

    /**
     * 쿠폰 템플릿이 적용된 모든 책 조회
     * @param templateId
     * @param page
     * @param size
     * @return Page<CouponBookDetailsBookTitleResponseDto>
     */
    @Override
    public Page<CouponBookDetailsBookTitleResponseDto> getAllCouponBooksForBook(Long templateId,
        int page, int size) {

        try {
            ResponseEntity<Page<CouponBookDetailsBookTitleResponseDto>> response = couponBookAdapter.findAllCouponBooksTitle(templateId, page, size);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponBookNotFoundException("CouponBook Not Found...!");
        } catch (RuntimeException e) {
            throw new CouponBookNotFoundException(e.getMessage());
        }
    }
}
