package shop.s5g.front.controller.coupon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.s5g.front.annotation.MemberAndAdminOnly;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.category.CouponCategoryDetailsCategoryNameDto;
import shop.s5g.front.dto.member.MemberInfoResponseDto;
import shop.s5g.front.service.coupon.category.CouponCategoryService;
import shop.s5g.front.service.coupon.coupon.CouponService;
import shop.s5g.front.service.member.MemberService;

@Slf4j
@Controller
@RequestMapping("/event/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final MemberService memberService;
    private final CouponCategoryService couponCategoryService;

    /**
     * 선착순 쿠폰 발급 - MQ
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/issue")
    @MemberAndAdminOnly
    public String getEventCoupon(RedirectAttributes redirectAttributes) {

        MemberInfoResponseDto loginMember =  memberService.getMemberInfo();

        MessageDto response = couponService.createEventCoupon(loginMember.customerId());

        if (Objects.isNull(response)) {
            redirectAttributes.addFlashAttribute("alertMessage", "쿠폰 발급에 실패했습니다.");
        } else {
            redirectAttributes.addFlashAttribute("alertMessage", "쿠폰이 성공적으로 발급되었습니다.");
        }

        return "redirect:/";
    }

    /**
     * 카테고리 쿠폰 발급 - MQ
     * @param redirectAttributes
     * @param couponCategoryDetailsCategoryNameDto
     * @return
     */
    @PostMapping("/category/issue")
    public String getCategoryCoupon(
        RedirectAttributes redirectAttributes,
        @ModelAttribute CouponCategoryDetailsCategoryNameDto couponCategoryDetailsCategoryNameDto) {

        MemberInfoResponseDto loginMember =  memberService.getMemberInfo();

        Map<String, Object> couponCategoryInfo = new HashMap<>();

        couponCategoryInfo.put("categoryName", couponCategoryDetailsCategoryNameDto.categoryName());
        couponCategoryInfo.put("memberId", loginMember.customerId());

        MessageDto response = couponService.createCategoryCoupon(couponCategoryInfo);

        if (Objects.isNull(response)) {
            redirectAttributes.addFlashAttribute("alertMessage", "쿠폰 발급에 실패했습니다.");
        } else {
            redirectAttributes.addFlashAttribute("alertMessage", "쿠폰이 성공적으로 발급되었습니다.");
        }

        return "redirect:/";
    }

    /**
     * 쿠폰 발급이 가능한 카테고리 데이터 조회
     * @param page
     * @return
     */
    @ResponseBody
    @GetMapping("/category")
    public List<CouponCategoryDetailsCategoryNameDto> getCouponCategoryList(
        @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 3);

        return couponCategoryService.getCategoryNamesForAppliedCouponTemplates(pageable).getContent();
    }
}
