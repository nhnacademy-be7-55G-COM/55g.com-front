package shop.s5g.front.service.coupon.category.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.coupon.CouponCategoryAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponCategoryDetailsCategoryNameDto;
import shop.s5g.front.dto.coupon.CouponCategoryRequestDto;
import shop.s5g.front.dto.coupon.CategoryResponseDto;
import shop.s5g.front.dto.coupon.CouponCategoryResponseDto;
import shop.s5g.front.exception.coupon.CouponCategoryNotFoundException;
import shop.s5g.front.service.coupon.category.CouponCategoryService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponCategoryServiceImpl implements CouponCategoryService {

    private final CouponCategoryAdapter couponCategoryAdapter;

    /**
     * 카테고리 쿠폰 생성
     * @param couponCategoryRequestDto
     * @return MessageDto
     */
    @Override
    public MessageDto createCouponCategory(CouponCategoryRequestDto couponCategoryRequestDto) {

        try {
            ResponseEntity<MessageDto> response = couponCategoryAdapter.createCouponCategory(couponCategoryRequestDto);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new CouponCategoryNotFoundException("Not Found Category List");
        } catch (RuntimeException e) {
            throw new CouponCategoryNotFoundException(e.getMessage());
        }
    }

    /**
     * 카테고리 조회
     * @return Page<CategoryResponseDto>
     */
    @Override
    public Page<CategoryResponseDto> getAllCategories(Pageable pageable) {

        try {
            ResponseEntity<Page<CategoryResponseDto>> categoryList = couponCategoryAdapter.findAllCategories(pageable);

            if (categoryList.getStatusCode().is2xxSuccessful()) {
                return categoryList.getBody();
            }

            throw new CouponCategoryNotFoundException("Not Found Category List");
        } catch (RuntimeException e) {
            throw new CouponCategoryNotFoundException(e.getMessage());
        }
    }

    /**
     * 쿠폰 카테고리 조회
     * @param pageable
     * @return Page<CouponCategoryResponseDto>
     */
    @Override
    public Page<CouponCategoryResponseDto> getAllCouponCategories(Pageable pageable) {

        try {
            ResponseEntity<Page<CouponCategoryResponseDto>> categoryList = couponCategoryAdapter.findAllCouponCategories(pageable);

            if (categoryList.getStatusCode().is2xxSuccessful()) {
                return categoryList.getBody();
            }

            throw new CouponCategoryNotFoundException("Not Found Category List");
        } catch (RuntimeException e) {
            throw new CouponCategoryNotFoundException(e.getMessage());
        }

    }

    /**
     * 쿠폰 적용된 카테고리 조회
     * @param pageable
     * @return Page<CouponCategoryDetailsCategoryNameDto>
     */
    @Override
    public Page<CouponCategoryDetailsCategoryNameDto> getCategoryNamesForAppliedCouponTemplates(Pageable pageable) {

        try {
            ResponseEntity<Page<CouponCategoryDetailsCategoryNameDto>> categoryList = couponCategoryAdapter.findCategoryNameByTemplate(pageable);

            if (categoryList.getStatusCode().is2xxSuccessful()) {
                return categoryList.getBody();
            }

            throw new CouponCategoryNotFoundException("Not Found Category List");
        } catch (RuntimeException e) {
            throw new CouponCategoryNotFoundException(e.getMessage());
        }
    }

    /**
     * 특정 카테고리 조회
     * @param categoryId
     * @return CouponCategoryResponseDto
     */
    @Override
    public CategoryResponseDto getCategory(Long categoryId) {

        try {

            ResponseEntity<CategoryResponseDto> category = couponCategoryAdapter.findCouponCategory(categoryId);

            if (category.getStatusCode().is2xxSuccessful()) {
                return category.getBody();
            }

            throw new CouponCategoryNotFoundException("Not Found Category");

        } catch (RuntimeException e) {
            throw new CouponCategoryNotFoundException(e.getMessage());
        }
    }
}
