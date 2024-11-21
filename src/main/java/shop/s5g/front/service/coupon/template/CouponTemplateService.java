package shop.s5g.front.service.coupon.template;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponTemplateInquiryResponseDto;
import shop.s5g.front.dto.coupon.CouponTemplateRegisterRequestDto;
import shop.s5g.front.dto.coupon.CouponTemplateUpdateRequestDto;

public interface CouponTemplateService {

    MessageDto createCouponTemplate(CouponTemplateRegisterRequestDto couponTemplateRegisterRequestDto);

    Page<CouponTemplateInquiryResponseDto> getCouponTemplates(Pageable pageable);

    Page<CouponTemplateInquiryResponseDto> getCouponTemplatesUnused(Pageable pageable);

    CouponTemplateInquiryResponseDto findCouponTemplateById(Long couponTemplateId);

    MessageDto deleteCouponTemplate(Long couponTemplateId);

    MessageDto updateCouponTemplate(Long couponTemplateId, CouponTemplateUpdateRequestDto couponTemplateUpdateRequestDto);
}
