package shop.s5g.front.adapter.coupon;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.coupon.CouponRegisterRequestDto;
import shop.s5g.front.dto.coupon.coupon.CouponResponseDto;

@FeignClient(name = "coupon", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface CouponAdapter {

    @PostMapping("/api/shop/admin/coupons")
    ResponseEntity<MessageDto> createCoupon(@RequestBody CouponRegisterRequestDto couponRegisterRequestDto);

    @GetMapping("/api/shop/admin/coupons")
    ResponseEntity<Page<CouponResponseDto>> getAllCoupons(Pageable pageable);

}
