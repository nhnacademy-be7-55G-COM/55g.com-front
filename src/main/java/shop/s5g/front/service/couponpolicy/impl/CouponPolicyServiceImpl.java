package shop.s5g.front.service.couponpolicy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.CouponPolicyAdapter;
import shop.s5g.front.dto.CouponPolicyRegisterRequestDto;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.exception.couponpolicy.CouponPolicyRegisterFailedException;
import shop.s5g.front.exception.member.MemberRegisterFailedException;
import shop.s5g.front.service.couponpolicy.CouponPolicyService;

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
            throw new MemberRegisterFailedException(e.getMessage());
        }
    }
}
