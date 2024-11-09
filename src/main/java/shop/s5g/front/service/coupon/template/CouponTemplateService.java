package shop.s5g.front.service.coupon.template;

import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponTemplateRegisterRequestDto;

public interface CouponTemplateService {

    MessageDto createCouponTemplate(CouponTemplateRegisterRequestDto couponTemplateRegisterRequestDto);
}
