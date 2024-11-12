package shop.s5g.front.service.coupon.book.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.CouponBookAdapter;
import shop.s5g.front.dto.coupon.CouponBookDetailsBookResponseDto;
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
    public Page<CouponBookDetailsBookResponseDto> getAllBooks(int page, int size) {

        try {
            ResponseEntity<Page<CouponBookDetailsBookResponseDto>> response = couponBookAdapter.findAllBooks(page, size);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponBookNotFoundException("CouponBook Not Found...!");
        } catch (RuntimeException e) {
            throw new CouponBookNotFoundException(e.getMessage());
        }
    }

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
