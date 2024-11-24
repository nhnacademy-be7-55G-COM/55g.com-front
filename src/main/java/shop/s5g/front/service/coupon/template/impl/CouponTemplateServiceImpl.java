package shop.s5g.front.service.coupon.template.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.coupon.CouponTemplateAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.template.CouponTemplateInquiryResponseDto;
import shop.s5g.front.dto.coupon.template.CouponTemplateRegisterRequestDto;
import shop.s5g.front.dto.coupon.template.CouponTemplateUpdateRequestDto;
import shop.s5g.front.exception.coupon.CouponTemplateNotFoundException;
import shop.s5g.front.exception.coupon.CouponTemplateRegisterFailedException;
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
     * @param pageable
     * @return List<CouponTemplateInquiryResponseDto>
     */
    @Override
    public Page<CouponTemplateInquiryResponseDto> getCouponTemplates(Pageable pageable) {

        try {
            ResponseEntity<Page<CouponTemplateInquiryResponseDto>> response = couponTemplateAdapter.findCouponTemplates(pageable);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponTemplateNotFoundException("CouponTemplate Not Found...!");
        }
         catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CouponTemplateRegisterFailedException(e.getMessage());
         }
    }

    /**
     * 사용되지 않은 쿠폰 템플릿 조회
     * @param pageable
     * @return Page<CouponTemplateInquiryResponseDto>
     */
    @Override
    public Page<CouponTemplateInquiryResponseDto> getCouponTemplatesUnused(Pageable pageable) {

        try {
            ResponseEntity<Page<CouponTemplateInquiryResponseDto>> response = couponTemplateAdapter.findCouponTemplatesUnused(pageable);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponTemplateNotFoundException("CouponTemplate Not Found...!");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CouponTemplateRegisterFailedException(e.getMessage());
        }
    }

    /**
     * 특정 쿠폰 템플릿 조회
     * @param couponTemplateId
     * @return CouponTemplateInquiryResponseDto
     */
    @Override
    public CouponTemplateInquiryResponseDto findCouponTemplateById(Long couponTemplateId) {

        try {
            ResponseEntity<CouponTemplateInquiryResponseDto> response = couponTemplateAdapter.findCouponTemplateById(couponTemplateId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponTemplateNotFoundException("CouponTemplate Not Found...!");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CouponTemplateRegisterFailedException(e.getMessage());
        }

    }

    /**
     * 쿠폰 템플릿 삭제
     * @param couponTemplateId
     * @return MessageDto
     */
    @Override
    public MessageDto deleteCouponTemplate(Long couponTemplateId) {

        try {
            ResponseEntity<MessageDto> response = couponTemplateAdapter.deleteCouponTemplate(couponTemplateId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new RuntimeException("Delete CouponTemplate Failed...");

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 쿠폰 템플릿 수정
     * @param couponTemplateId
     * @return MessageDto
     */
    @Override
    public MessageDto updateCouponTemplate(Long couponTemplateId, CouponTemplateUpdateRequestDto couponTemplateUpdateRequestDto) {
        try {
            ResponseEntity<MessageDto> response = couponTemplateAdapter.updateCouponTemplate(couponTemplateId, couponTemplateUpdateRequestDto);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new RuntimeException("Update CouponTemplate Failed...");

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
