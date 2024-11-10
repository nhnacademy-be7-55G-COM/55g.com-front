package shop.s5g.front.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.dto.coupon.CouponPolicyInquiryResponseDto;
import shop.s5g.front.dto.coupon.CouponPolicyRegisterRequestDto;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperView;
import shop.s5g.front.service.couponpolicy.impl.CouponPolicyServiceImpl;
import shop.s5g.front.service.delivery.DeliveryFeeService;
import shop.s5g.front.service.wrappingpaper.WrappingPaperService;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final CouponPolicyServiceImpl couponPolicyService;
    private final DeliveryFeeService deliveryFeeService;
    private final WrappingPaperService wrappingPaperService;

    @GetMapping("/admin")
    public String adminPage() {
        return "admin/admin";
    }

    @GetMapping("/admin/settings")
    public String adminSettingsPage() {
        return "admin/settings";
    }

    @PostMapping("/api/shop/admin/coupons/policy")
    public String adminCouponPolicyCreate(@ModelAttribute CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto) {
        couponPolicyService.createCouponPolicy(couponPolicyRegisterRequestDto);
        return "redirect:/admin";
    }

    @GetMapping("/api/shop/admin/coupons/policy")
    public ModelAndView adminCouponPoliciesList() {
        ModelAndView mv = new ModelAndView("/admin/inquiry");

        List<CouponPolicyInquiryResponseDto> couponPolicyList = couponPolicyService.findCouponPolices();

        mv.addObject("couponPolicyList", couponPolicyList);

        return mv;
    }

    @GetMapping("/admin/delivery-fee")
    public ModelAndView adminDeliveryFeePage() {
        ModelAndView mv = new ModelAndView("admin/delivery-fee");
        mv.addObject("feeList", deliveryFeeService.getAllFees());

        return mv;
    }

    @GetMapping("/admin/wrappingpaper")
    public ModelAndView adminWrappingPaperPage() {
        ModelAndView mv = new ModelAndView("admin/wrappingpaper");
        List<WrappingPaperView> views =
            wrappingPaperService.fetchAllPapers().stream()
                .map(wrappingPaperService::convertToView)
                .toList();
        mv.addObject("paperList", views);

        return mv;
    }
}
