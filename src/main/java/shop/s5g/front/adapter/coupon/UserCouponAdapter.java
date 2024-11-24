package shop.s5g.front.adapter.coupon;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.coupon.user.InValidUserCouponResponseDto;
import shop.s5g.front.dto.coupon.user.ValidUserCouponResponseDto;

@FeignClient(name = "userCoupon", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface UserCouponAdapter {

    @GetMapping("/api/shop/member/coupons/{userId}")
    ResponseEntity<Page<ValidUserCouponResponseDto>> findUserCouponList(@PathVariable("userId") Long customerId, Pageable pageable);

    @GetMapping("/api/shop/member/coupons/used/{userId}")
    ResponseEntity<Page<InValidUserCouponResponseDto>> findUsedCouponList(@PathVariable("userId") Long customerId, Pageable pageable);

    @GetMapping("/api/shop/member/coupons/expired/{userId}")
    ResponseEntity<Page<InValidUserCouponResponseDto>> findExpiredCouponList(@PathVariable("userId") Long customerId, Pageable pageable);

    @GetMapping("/api/shop/member/coupons/invalid/{userId}")
    ResponseEntity<Page<InValidUserCouponResponseDto>> findInvalidCouponList(@PathVariable("userId") Long customerId, Pageable pageable);

}
