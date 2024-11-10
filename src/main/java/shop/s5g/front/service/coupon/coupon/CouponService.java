package shop.s5g.front.service.coupon.coupon;

import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponRegisterRequestDto;

public interface CouponService {

    MessageDto createCoupon(CouponRegisterRequestDto couponRegisterRequestDto);
}
