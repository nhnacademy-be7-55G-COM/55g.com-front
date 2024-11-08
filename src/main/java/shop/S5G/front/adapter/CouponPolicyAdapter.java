package shop.S5G.front.adapter;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.S5G.front.dto.coupon.CouponPolicyInquiryResponseDto;
import shop.S5G.front.dto.coupon.CouponPolicyRegisterRequestDto;
import shop.S5G.front.dto.MessageDto;

@FeignClient(name = "couponPolicy", url = "${gateway.url}")
public interface CouponPolicyAdapter {

    @PostMapping("/api/shop/admin/coupons/policy")
    ResponseEntity<MessageDto> createCouponPolicy(@RequestBody CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto);

    @GetMapping("/api/shop/admin/coupons/policy")
    ResponseEntity<List<CouponPolicyInquiryResponseDto>> findCouponPolices();
}
