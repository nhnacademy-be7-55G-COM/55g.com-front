package shop.s5g.front.adapter.coupon;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.book.BookDetailsBookResponseDto;
import shop.s5g.front.dto.coupon.book.CouponBookDetailsBookTitleResponseDto;
import shop.s5g.front.dto.coupon.book.CouponBookRequestDto;
import shop.s5g.front.dto.coupon.book.CouponBookResponseDto;
import shop.s5g.front.dto.coupon.template.CouponTemplateInquiryResponseDto;

@FeignClient(name = "couponBook", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface CouponBookAdapter {

    @GetMapping("/api/shop/book/{bookId}")
    ResponseEntity<BookDetailsBookResponseDto> findBook(@PathVariable("bookId") Long bookId);

    @GetMapping("/api/shop/books/pageable")
    ResponseEntity<Page<BookDetailsBookResponseDto>> findAllBooks(
        Pageable pageable);

    @GetMapping("/api/shop/admin/coupons/books")
    ResponseEntity<Page<CouponBookResponseDto>> findAllCouponBooks(Pageable pageable);

    @GetMapping("/api/shop/admin/coupons/books/{bookId}")
    ResponseEntity<Page<CouponTemplateInquiryResponseDto>> findAllCouponBooksCouponTemplate(
        @PathVariable("bookId") Long bookId,
        Pageable pageable
    );

    @GetMapping("/api/shop/admin/coupons/books/template/{templateId}")
    ResponseEntity<Page<CouponBookDetailsBookTitleResponseDto>> findAllCouponBooksTitle(
        @PathVariable("templateId") Long templateId,
        Pageable pageable
    );

    @PostMapping("/api/shop/admin/coupons/book")
    ResponseEntity<MessageDto> createCouponBooks(CouponBookRequestDto couponBookRequestDto);

}
