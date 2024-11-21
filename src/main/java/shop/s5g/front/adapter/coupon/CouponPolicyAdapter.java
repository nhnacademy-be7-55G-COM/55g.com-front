package shop.s5g.front.adapter.coupon;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponPolicyInquiryResponseDto;
import shop.s5g.front.dto.coupon.CouponPolicyRegisterRequestDto;

@FeignClient(name = "couponPolicy", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface CouponPolicyAdapter {

    @PostMapping("/api/shop/admin/coupons/policy")
    ResponseEntity<MessageDto> createCouponPolicy(@RequestBody CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto);

    @GetMapping("/api/shop/admin/coupons/policy")
    ResponseEntity<Page<CouponPolicyInquiryResponseDto>> findCouponPolices(Pageable pageable);

    @GetMapping("/api/shop/admin/coupons/policy/{couponPolicyId}")
    ResponseEntity<CouponPolicyInquiryResponseDto> findCouponPolicy(@PathVariable("couponPolicyId") Long couponPolicyId);

    @PostMapping("/api/shop/admin/coupons/policy/{couponPolicyId}")
    ResponseEntity<MessageDto> updateCouponPolicy(
        @PathVariable("couponPolicyId") Long couponPolicyId,
        @RequestBody CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto);
}
