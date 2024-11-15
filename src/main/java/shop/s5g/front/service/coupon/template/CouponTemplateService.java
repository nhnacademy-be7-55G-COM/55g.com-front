package shop.s5g.front.service.coupon.template;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponTemplateInquiryResponseDto;
import shop.s5g.front.dto.coupon.CouponTemplateRegisterRequestDto;

public interface CouponTemplateService {

    MessageDto createCouponTemplate(CouponTemplateRegisterRequestDto couponTemplateRegisterRequestDto);

    List<CouponTemplateInquiryResponseDto> getCouponTemplates(int page, int size);

    Page<CouponTemplateInquiryResponseDto> getCouponTemplatesUnused(Pageable pageable);

    CouponTemplateInquiryResponseDto findCouponTemplateById(Long couponTemplateId);
}
