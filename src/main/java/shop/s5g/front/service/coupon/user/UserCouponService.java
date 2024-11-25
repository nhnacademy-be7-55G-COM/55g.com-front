package shop.s5g.front.service.coupon.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.front.dto.coupon.user.InValidUserCouponResponseDto;
import shop.s5g.front.dto.coupon.user.ValidUserCouponResponseDto;

public interface UserCouponService {

    Page<ValidUserCouponResponseDto> getUserCoupons(Long customerId, Pageable pageable);

    Page<InValidUserCouponResponseDto> getUsedCoupons(Long customerId, Pageable pageable);

    Page<InValidUserCouponResponseDto> getExpiredCoupons(Long customerId, Pageable pageable);

    Page<InValidUserCouponResponseDto> getInvalidCoupons(Long customerId, Pageable pageable);
}
