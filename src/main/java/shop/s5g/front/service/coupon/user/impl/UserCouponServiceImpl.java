package shop.s5g.front.service.coupon.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.coupon.UserCouponAdapter;
import shop.s5g.front.dto.coupon.user.InValidUserCouponResponseDto;
import shop.s5g.front.dto.coupon.user.ValidUserCouponResponseDto;
import shop.s5g.front.service.coupon.user.UserCouponService;

@Service
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {

    private final UserCouponAdapter userCouponAdapter;

    /**
     * 유저의 쿠폰 조회
     * @param customerId
     * @param pageable
     * @return Page<UserCouponResponseDto>
     */
    @Override
    public Page<ValidUserCouponResponseDto> getUserCoupons(Long customerId, Pageable pageable) {

        try {

            ResponseEntity<Page<ValidUserCouponResponseDto>> response = userCouponAdapter.findUserCouponList(customerId, pageable);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new RuntimeException("해당 유저의 쿠폰을 조회할 수 없습니다.");

        } catch (HttpServerErrorException | HttpClientErrorException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     *
     * @param customerId
     * @param pageable
     * @return
     */
    @Override
    public Page<InValidUserCouponResponseDto> getUsedCoupons(Long customerId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<InValidUserCouponResponseDto> getExpiredCoupons(Long customerId,
        Pageable pageable) {
        return null;
    }

    @Override
    public Page<InValidUserCouponResponseDto> getInvalidCoupons(Long customerId,
        Pageable pageable) {
        return null;
    }
}
