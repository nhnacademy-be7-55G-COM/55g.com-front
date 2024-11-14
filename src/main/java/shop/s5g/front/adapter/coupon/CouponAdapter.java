package shop.s5g.front.adapter.coupon;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponRegisterRequestDto;

@FeignClient(name = "coupon", url = "${gateway.url}")
public interface CouponAdapter {

    @PostMapping("/api/shop/admin/coupons")
    ResponseEntity<MessageDto> createCoupon(@RequestBody CouponRegisterRequestDto couponRegisterRequestDto);

}
