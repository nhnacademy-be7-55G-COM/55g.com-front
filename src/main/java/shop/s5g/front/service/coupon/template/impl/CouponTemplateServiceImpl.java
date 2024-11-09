package shop.s5g.front.service.coupon.template.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.CouponTemplateAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponTemplateRegisterRequestDto;
import shop.s5g.front.exception.couponpolicy.CouponPolicyRegisterFailedException;
import shop.s5g.front.service.coupon.template.CouponTemplateService;

@Service
@RequiredArgsConstructor
public class CouponTemplateServiceImpl implements CouponTemplateService {

    private final CouponTemplateAdapter couponTemplateAdapter;

    @Override
    public MessageDto createCouponTemplate(
        CouponTemplateRegisterRequestDto couponTemplateRegisterRequestDto) {
        try {
            ResponseEntity<MessageDto> response = couponTemplateAdapter.createCouponTemplate(couponTemplateRegisterRequestDto);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponPolicyRegisterFailedException("CouponTemplate Registration Failed");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CouponPolicyRegisterFailedException(e.getMessage());
        }
    }
}
