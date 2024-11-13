package shop.s5g.front.adapter.coupon;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.coupon.CouponBookDetailsBookResponseDto;
import shop.s5g.front.dto.coupon.CouponBookDetailsBookTitleResponseDto;
import shop.s5g.front.dto.coupon.CouponBookResponseDto;
import shop.s5g.front.dto.coupon.CouponTemplateInquiryResponseDto;

@FeignClient(name = "couponBook", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface CouponBookAdapter {

    @GetMapping("/api/shop/book/{bookId}")
    ResponseEntity<CouponBookDetailsBookResponseDto> findBook(@PathVariable("bookId") Long bookId);

    @GetMapping("/api/shop/books/pageable")
    ResponseEntity<Page<CouponBookDetailsBookResponseDto>> findAllBooks(
        Pageable pageable);

    @GetMapping("/api/shop/admin/coupons/books")
    ResponseEntity<Page<CouponBookResponseDto>> findAllCouponBooks(
        @RequestParam("page") int page,
        @RequestParam("size") int size);

    @GetMapping("/api/shop/admin/coupons/books/{bookId}")
    ResponseEntity<Page<CouponTemplateInquiryResponseDto>> findAllCouponBooksCouponTemplate(
        @PathVariable("bookId") Long bookId,
        @RequestParam("page") int page,
        @RequestParam("size") int size
    );

    @GetMapping("/api/shop/admin/coupons/books/template/{templateId}")
    ResponseEntity<Page<CouponBookDetailsBookTitleResponseDto>> findAllCouponBooksTitle(
        @PathVariable("templateId") Long templateId,
        @RequestParam("page") int page,
        @RequestParam("size") int size
    );

}
