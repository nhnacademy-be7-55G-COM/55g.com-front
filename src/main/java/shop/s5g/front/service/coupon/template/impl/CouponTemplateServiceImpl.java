package shop.s5g.front.service.coupon.template.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.CouponTemplateAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponTemplateInquiryResponseDto;
import shop.s5g.front.dto.coupon.CouponTemplateRegisterRequestDto;
import shop.s5g.front.exception.couponpolicy.CouponTemplateNotFoundException;
import shop.s5g.front.exception.couponpolicy.CouponTemplateRegisterFailedException;
import shop.s5g.front.service.coupon.template.CouponTemplateService;

@Service
@RequiredArgsConstructor
public class CouponTemplateServiceImpl implements CouponTemplateService {

    private final CouponTemplateAdapter couponTemplateAdapter;

    /**
     * 쿠폰 템플릿 생성
     * @param couponTemplateRegisterRequestDto
     * @return MessageDto
     */
    @Override
    public MessageDto createCouponTemplate(
        CouponTemplateRegisterRequestDto couponTemplateRegisterRequestDto) {
        try {
            ResponseEntity<MessageDto> response = couponTemplateAdapter.createCouponTemplate(couponTemplateRegisterRequestDto);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponTemplateRegisterFailedException("CouponTemplate Registration Failed");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CouponTemplateRegisterFailedException(e.getMessage());
        }
    }

    /**
     * 쿠폰 템플릿 조회
     * @param page
     * @param size
     * @return List<CouponTemplateInquiryResponseDto>
     */
    @Override
    public List<CouponTemplateInquiryResponseDto> findCouponTemplates(int page, int size) {

        try {
            ResponseEntity<List<CouponTemplateInquiryResponseDto>> response = couponTemplateAdapter.findCouponTemplates(page, size);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponTemplateNotFoundException("CouponTemplate Not Found...!");
        }
         catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CouponTemplateRegisterFailedException(e.getMessage());
         }
    }
}
