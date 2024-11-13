package shop.s5g.front.service.coupon.coupon.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.coupon.CouponAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponRegisterRequestDto;
import shop.s5g.front.exception.coupon.CouponRegisterFailedException;
import shop.s5g.front.service.coupon.coupon.CouponService;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponAdapter couponAdapter;

    //TODO (young ho) - 나중에 에러처리 수정예정
    @Override
    public MessageDto createCoupon(CouponRegisterRequestDto couponRegisterRequestDto) {
        try {
            ResponseEntity<MessageDto> response = couponAdapter.createCoupon(couponRegisterRequestDto);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponRegisterFailedException("Coupon Registration Failed");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CouponRegisterFailedException(e.getMessage());
        }
    }
}
