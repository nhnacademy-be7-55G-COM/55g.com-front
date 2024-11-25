package shop.s5g.front.service.coupon.book.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.coupon.CouponBookAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.book.BookDetailsBookResponseDto;
import shop.s5g.front.dto.coupon.book.CouponBookDetailsBookTitleResponseDto;
import shop.s5g.front.dto.coupon.book.CouponBookRequestDto;
import shop.s5g.front.dto.coupon.book.CouponBookResponseDto;
import shop.s5g.front.dto.coupon.template.CouponTemplateInquiryResponseDto;
import shop.s5g.front.exception.coupon.CouponBookNotFoundException;
import shop.s5g.front.service.coupon.book.CouponBookService;

@Service
@RequiredArgsConstructor
public class CouponBookServiceImpl implements CouponBookService {

    private final CouponBookAdapter couponBookAdapter;

    /**
     * 책 상세 페이지 조회
     * @param bookId
     * @return ResponseEntity<BookDetailsBookResponseDto>
     */
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
     * @param pageable
     * @return Page<CouponBookResponseDto>
     */
    @Override
    public Page<CouponBookResponseDto> getAllCouponBooks(Pageable pageable) {
        try {
            ResponseEntity<Page<CouponBookResponseDto>> response = couponBookAdapter.findAllCouponBooks(pageable);

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
     * @param pageable
     * @return Page<CouponTemplateInquiryResponseDto>
     */
    @Override
    public Page<CouponTemplateInquiryResponseDto> getAllCouponBooksForCouponTemplate(Long bookId,
        Pageable pageable) {

        try {
            ResponseEntity<Page<CouponTemplateInquiryResponseDto>> response = couponBookAdapter.findAllCouponBooksCouponTemplate(bookId, pageable);

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
     * @param pageable
     * @return Page<CouponBookDetailsBookTitleResponseDto>
     */
    @Override
    public Page<CouponBookDetailsBookTitleResponseDto> getAllCouponBooksForBook(Long templateId,
        Pageable pageable) {

        try {
            ResponseEntity<Page<CouponBookDetailsBookTitleResponseDto>> response = couponBookAdapter.findAllCouponBooksTitle(templateId, pageable);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponBookNotFoundException("CouponBook Not Found...!");
        } catch (RuntimeException e) {
            throw new CouponBookNotFoundException(e.getMessage());
        }
    }

    /**
     * 책 쿠폰 생성
     * @param couponBookRequestDto
     * @return MessageDto
     */
    @Override
    public MessageDto createCouponBook(CouponBookRequestDto couponBookRequestDto) {
        try {
            ResponseEntity<MessageDto> response = couponBookAdapter.createCouponBooks(couponBookRequestDto);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponBookNotFoundException("CouponBook Create Failed...!");
        } catch (RuntimeException e) {
            throw new CouponBookNotFoundException(e.getMessage());
        }
    }
}
