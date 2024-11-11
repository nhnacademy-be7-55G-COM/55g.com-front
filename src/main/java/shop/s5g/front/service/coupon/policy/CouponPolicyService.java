package shop.s5g.front.service.coupon.policy;

import java.util.List;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponPolicyInquiryResponseDto;
import shop.s5g.front.dto.coupon.CouponPolicyRegisterRequestDto;

public interface CouponPolicyService {
    MessageDto createCouponPolicy(CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto);
    List<CouponPolicyInquiryResponseDto> findCouponPolices();
}
