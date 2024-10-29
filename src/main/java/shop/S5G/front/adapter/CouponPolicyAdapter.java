package shop.S5G.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.S5G.front.dto.CouponPolicyRegisterRequestDto;
import shop.S5G.front.dto.MessageDto;

@FeignClient(value = "couponPolicy", url = "${gateway.url}")
public interface CouponPolicyAdapter {

    @PostMapping("/api/admin/coupons/policy")
    ResponseEntity<MessageDto> createCouponPolicy(@RequestBody CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto);
}
