package shop.S5G.front.service.couponpolicy;

import shop.S5G.front.dto.CouponPolicyRegisterRequestDto;
import shop.S5G.front.dto.MessageDto;

public interface CouponPolicyService {
    MessageDto createCouponPolicy(CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto);
}
