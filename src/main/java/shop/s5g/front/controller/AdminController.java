package shop.s5g.front.controller;

import java.util.List;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.dto.coupon.CouponPolicyInquiryResponseDto;
import shop.s5g.front.dto.coupon.CouponPolicyRegisterRequestDto;
import org.springframework.web.bind.annotation.RequestParam;
import shop.s5g.front.dto.coupon.CouponRegisterRequestDto;
import shop.s5g.front.dto.coupon.CouponTemplateInquiryResponseDto;
import shop.s5g.front.dto.coupon.CouponTemplateRegisterRequestDto;
import shop.s5g.front.dto.order.OrderQueryRequestDto;
import shop.s5g.front.dto.order.OrderWithDetailResponseDto;
import shop.s5g.front.service.coupon.coupon.impl.CouponServiceImpl;
import shop.s5g.front.service.coupon.policy.impl.CouponPolicyServiceImpl;
import shop.s5g.front.service.coupon.template.impl.CouponTemplateServiceImpl;
import shop.s5g.front.service.order.OrderDetailService;
import shop.s5g.front.service.order.OrderService;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final CouponPolicyServiceImpl couponPolicyService;
    private final CouponTemplateServiceImpl couponTemplateService;
    private final CouponServiceImpl couponService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    @GetMapping("/admin")
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

    // Primary Key인 customer id로 검색함. 비회원과 회원 모두 검색하게 하기 위함.
    @GetMapping("/admin/order")
    public ModelAndView adminManageOrderQuery(@RequestParam(required = false) OrderQueryRequestDto queryRequest) {
        ModelAndView mv = new ModelAndView("admin/order-list");
        if (queryRequest == null) {
            mv.addObject("startDate", LocalDate.now().minusMonths(1));
            mv.addObject("endDate", LocalDate.now());
            mv.addObject("customerId", "");
            mv.addObject("orderList", List.of());
        } else {
            List<OrderWithDetailResponseDto> response = orderService.queryOrdersBetweenDates(
                queryRequest);

            mv.addObject("startDate", queryRequest.startDate());
            mv.addObject("endDate", queryRequest.endDate());
            mv.addObject("customerId", queryRequest.customerId());
            mv.addObject("orderList", response);
        }
        return mv;
    }

    @GetMapping("/admin/order/{orderId}")
    public ModelAndView adminManageOrderDetail(@PathVariable long orderId) {
        ModelAndView mv = new ModelAndView("admin/order-detail");
        mv.addObject("info", orderDetailService.getOrderDetailAllInfos(orderId));
        return mv;
    }

    @GetMapping("/api/shop/admin/coupons/policy")
    public ModelAndView adminCouponPoliciesList() {
        ModelAndView mv = new ModelAndView("policyInquiry");

        List<CouponPolicyInquiryResponseDto> couponPolicyList = couponPolicyService.findCouponPolices();

        mv.addObject("couponPolicyList", couponPolicyList);

        return mv;
    }

}
