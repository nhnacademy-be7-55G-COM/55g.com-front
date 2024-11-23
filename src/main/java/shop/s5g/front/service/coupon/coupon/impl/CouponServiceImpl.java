package shop.s5g.front.service.coupon.coupon.impl;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.coupon.CouponAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponRegisterRequestDto;
import shop.s5g.front.dto.coupon.CouponResponseDto;
import shop.s5g.front.exception.coupon.CouponRegisterFailedException;
import shop.s5g.front.service.coupon.coupon.CouponService;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponAdapter couponAdapter;

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

    @Override
    public MessageDto createEventCoupon(Long customerId) {
        return null;
    }

    @Override
    public MessageDto createCategoryCoupon(Map<String, Object> categoryCouponMap) {
        return null;
    }

    /**
     * 쿠폰 조회하기
     * @param pageable
     * @return Page<CouponResponseDto>
     */
    @Override
    public Page<CouponResponseDto> getAllCoupons(Pageable pageable) {

        try {
            ResponseEntity<Page<CouponResponseDto>> response = couponAdapter.getAllCoupons(pageable);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new RuntimeException("쿠폰 조회에 실패했습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
