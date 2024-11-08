package shop.s5g.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.dto.CouponPolicyRegisterRequestDto;
import shop.s5g.front.dto.MessageDto;

@FeignClient(value = "couponPolicy", url = "${gateway.url}")
public interface CouponPolicyAdapter {

    @PostMapping("/api/shop/admin/coupons/policy")
    ResponseEntity<MessageDto> createCouponPolicy(@RequestBody CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto);
}
