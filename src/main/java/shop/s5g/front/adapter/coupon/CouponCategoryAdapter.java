package shop.s5g.front.adapter.coupon;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponCategoryDetailsCategoryNameDto;
import shop.s5g.front.dto.coupon.CouponCategoryRequestDto;
import shop.s5g.front.dto.coupon.CategoryResponseDto;
import shop.s5g.front.dto.coupon.CouponCategoryResponseDto;

@FeignClient(name = "couponCategory", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface CouponCategoryAdapter {

    @GetMapping("/api/shop/category")
    ResponseEntity<Page<CategoryResponseDto>> findAllCategories(Pageable pageable);

    @GetMapping("/api/shop/admin/coupons/category/{categoryId}")
    ResponseEntity<CategoryResponseDto> findCouponCategory(@PathVariable("categoryId") Long categoryId);

    @PostMapping("/api/shop/admin/coupons/category")
    ResponseEntity<MessageDto> createCouponCategory(@RequestBody CouponCategoryRequestDto couponCategoryRequestDto);

    @GetMapping("/api/shop/admin/coupons/categories")
    ResponseEntity<Page<CouponCategoryResponseDto>> findAllCouponCategories(Pageable pageable);

    @GetMapping("/api/shop/admin/coupons/categories/name/templates")
    ResponseEntity<Page<CouponCategoryDetailsCategoryNameDto>> findCategoryNameByTemplate(Pageable pageable);
}
