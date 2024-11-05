package shop.S5G.front.controller;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import shop.S5G.front.dto.CouponPolicyRegisterRequestDto;
import shop.S5G.front.dto.order.OrderQueryRequestDto;
import shop.S5G.front.dto.order.OrderWithDetailResponseDto;
import shop.S5G.front.service.couponpolicy.impl.CouponPolicyServiceImpl;
import shop.S5G.front.service.order.OrderDetailService;
import shop.S5G.front.service.order.OrderService;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final CouponPolicyServiceImpl couponPolicyService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

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
}
