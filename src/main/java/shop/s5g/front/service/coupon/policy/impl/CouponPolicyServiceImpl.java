package shop.s5g.front.service.coupon.policy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.coupon.CouponPolicyAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponPolicyInquiryResponseDto;
import shop.s5g.front.dto.coupon.CouponPolicyRegisterRequestDto;
import shop.s5g.front.exception.coupon.CouponPolicyNotFoundException;
import shop.s5g.front.exception.coupon.CouponPolicyRegisterFailedException;
import shop.s5g.front.service.coupon.policy.CouponPolicyService;

@Service
@RequiredArgsConstructor
public class CouponPolicyServiceImpl implements CouponPolicyService {

    private final CouponPolicyAdapter couponPolicyAdapter;

    @Override
    public MessageDto createCouponPolicy(
        CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto) {
        try {
            ResponseEntity<MessageDto> response = couponPolicyAdapter.createCouponPolicy(couponPolicyRegisterRequestDto);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponPolicyRegisterFailedException("CouponPolicy Registration Failed");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CouponPolicyRegisterFailedException(e.getMessage());
        }
    }

    @Override
    public Page<CouponPolicyInquiryResponseDto> findCouponPolices(Pageable pageable) {
        try {
            ResponseEntity<Page<CouponPolicyInquiryResponseDto>> response = couponPolicyAdapter.findCouponPolices(pageable);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponPolicyNotFoundException("Not Exists Coupon Policy");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CouponPolicyNotFoundException(e.getMessage());
        }
    }
}
