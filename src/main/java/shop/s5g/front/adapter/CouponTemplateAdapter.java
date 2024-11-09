package shop.s5g.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponTemplateRegisterRequestDto;

@FeignClient(name = "couponTemplate", url = "${gateway.url}")
public interface CouponTemplateAdapter {

    @PostMapping("/api/shop/admin/coupons/template")
    ResponseEntity<MessageDto> createCouponTemplate(@RequestBody CouponTemplateRegisterRequestDto couponTemplateRegisterRequestDto);
}
