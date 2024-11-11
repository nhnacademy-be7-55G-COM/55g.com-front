package shop.s5g.front.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.annotation.RedirectWithAlert;
import shop.s5g.front.dto.coupon.CouponPolicyInquiryResponseDto;
import shop.s5g.front.dto.coupon.CouponPolicyRegisterRequestDto;
import shop.s5g.front.dto.coupon.CouponRegisterRequestDto;
import shop.s5g.front.dto.coupon.CouponTemplateInquiryResponseDto;
import shop.s5g.front.dto.coupon.CouponTemplateRegisterRequestDto;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperView;
import shop.s5g.front.exception.auth.UnauthorizedException;
import shop.s5g.front.service.coupon.coupon.impl.CouponServiceImpl;
import shop.s5g.front.service.coupon.policy.impl.CouponPolicyServiceImpl;
import shop.s5g.front.service.coupon.template.impl.CouponTemplateServiceImpl;
import shop.s5g.front.service.delivery.DeliveryFeeService;
import shop.s5g.front.service.wrappingpaper.WrappingPaperService;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final CouponPolicyServiceImpl couponPolicyService;
    private final CouponTemplateServiceImpl couponTemplateService;
    private final CouponServiceImpl couponService;
    private final DeliveryFeeService deliveryFeeService;
    private final WrappingPaperService wrappingPaperService;

    @GetMapping("/admin")
    @RedirectWithAlert(redirect = "/", title = "권한 없음", exceptions = UnauthorizedException.class)
    public ModelAndView adminPage() {

        ModelAndView mv = new ModelAndView("/admin/admin");

        List<CouponPolicyInquiryResponseDto> couponPolicyList = couponPolicyService.findCouponPolices();

        mv.addObject("couponPolicyList", couponPolicyList);

        return mv;
    }

    @GetMapping("/admin/settings")
    public String adminSettingsPage() {
        return "admin/settings";
    }

    @GetMapping("/api/shop/admin/coupons/template/{templateId}")
    public String adminCouponCreate(@PathVariable("templateId") Long templateId, Model model) {

        CouponTemplateInquiryResponseDto couponTemplate = couponTemplateService.findCouponTemplateById(templateId);

        model.addAttribute("couponTemplate", couponTemplate);
        model.addAttribute("couponTemplateId", templateId);

        return "admin/coupon";
    }

    @GetMapping("/api/shop/admin/coupons/templates")
    public String adminCouponTemplatePage(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "15") int size,
        Model model
        ) {
        List<CouponTemplateInquiryResponseDto> couponTemplateList = couponTemplateService.findCouponTemplates(page, size);

        model.addAttribute("couponTemplate", couponTemplateList);
        model.addAttribute("currentPage", page);

        return "admin/templateInquiry";
    }

    @PostMapping("/api/shop/admin/coupons/template")
    public String adminCouponTemplateCreate(@ModelAttribute CouponTemplateRegisterRequestDto couponTemplateRegisterRequestDto) {
        couponTemplateService.createCouponTemplate(couponTemplateRegisterRequestDto);
        return "redirect:/admin";
    }

    @PostMapping("/api/shop/admin/coupons/policy")
    public String adminCouponPolicyCreate(@ModelAttribute CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto) {
        couponPolicyService.createCouponPolicy(couponPolicyRegisterRequestDto);
        return "redirect:/admin";
    }

    @PostMapping("/api/shop/admin/coupons")
    public String adminCouponCreate(@ModelAttribute CouponRegisterRequestDto couponRegisterRequestDto) {
        couponService.createCoupon(couponRegisterRequestDto);
        return "redirect:/admin";
    }

    @GetMapping("/api/shop/admin/coupons/policy")
    public ModelAndView adminCouponPoliciesList() {
        ModelAndView mv = new ModelAndView("admin/policyInquiry");

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
