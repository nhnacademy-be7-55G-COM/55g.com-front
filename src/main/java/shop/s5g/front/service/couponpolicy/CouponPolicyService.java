package shop.s5g.front.service.couponpolicy;

import shop.s5g.front.dto.coupon.CouponPolicyRegisterRequestDto;
import shop.s5g.front.dto.MessageDto;

public interface CouponPolicyService {
    MessageDto createCouponPolicy(CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto);
}
