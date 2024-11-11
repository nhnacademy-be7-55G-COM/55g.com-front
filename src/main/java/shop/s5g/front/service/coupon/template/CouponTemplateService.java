package shop.s5g.front.service.coupon.template;

import java.util.List;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponTemplateInquiryResponseDto;
import shop.s5g.front.dto.coupon.CouponTemplateRegisterRequestDto;

public interface CouponTemplateService {

    MessageDto createCouponTemplate(CouponTemplateRegisterRequestDto couponTemplateRegisterRequestDto);

    List<CouponTemplateInquiryResponseDto> findCouponTemplates(int page, int size);

    CouponTemplateInquiryResponseDto findCouponTemplateById(Long couponTemplateId);
}
