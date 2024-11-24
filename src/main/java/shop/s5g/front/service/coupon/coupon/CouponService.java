package shop.s5g.front.service.coupon.coupon;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.coupon.CouponRegisterRequestDto;
import shop.s5g.front.dto.coupon.coupon.CouponResponseDto;

public interface CouponService {

    MessageDto createCoupon(CouponRegisterRequestDto couponRegisterRequestDto);

    MessageDto createEventCoupon(Long customerId);

    MessageDto createCategoryCoupon(Map<String, Object> categoryCouponMap);

    Page<CouponResponseDto> getAllCoupons(Pageable pageable);
}
