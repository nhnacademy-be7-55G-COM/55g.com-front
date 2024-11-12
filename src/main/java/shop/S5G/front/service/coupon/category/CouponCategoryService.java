package shop.s5g.front.service.coupon.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponCategoryRequestDto;
import shop.s5g.front.dto.coupon.CouponCategoryResponseDto;

public interface CouponCategoryService {

    // Create
    MessageDto createCouponCategory(CouponCategoryRequestDto couponCategoryRequestDto);

    // get
    Page<CouponCategoryResponseDto> getAllCategories(Pageable pageable);

    CouponCategoryResponseDto getCategory(Long categoryId);
}
