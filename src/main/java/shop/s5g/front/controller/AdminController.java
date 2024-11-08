package shop.s5g.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.dto.CouponPolicyRegisterRequestDto;
import shop.s5g.front.service.couponpolicy.impl.CouponPolicyServiceImpl;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final CouponPolicyServiceImpl couponPolicyService;

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }

    @GetMapping("/admin/settings")
    public String adminSettingsPage() {
        return "settings";
    }

    @PostMapping("/admin/coupons/policy")
    public String adminCouponPolicyCreate(@RequestBody CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto) {
        couponPolicyService.createCouponPolicy(couponPolicyRegisterRequestDto);
        return "redirect:/admin";
    }

}
