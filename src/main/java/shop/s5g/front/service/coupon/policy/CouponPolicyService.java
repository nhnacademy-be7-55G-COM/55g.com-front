package shop.s5g.front.service.coupon.policy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponPolicyInquiryResponseDto;
import shop.s5g.front.dto.coupon.CouponPolicyRegisterRequestDto;

public interface CouponPolicyService {
    MessageDto createCouponPolicy(CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto);
    Page<CouponPolicyInquiryResponseDto> findCouponPolices(Pageable pageable);
    CouponPolicyInquiryResponseDto findCouponPolicy(Long couponPolicyId);
    MessageDto updateCouponPolicy(Long couponPolicyId, CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto);
}
