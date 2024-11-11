package shop.s5g.front.controller;

import feign.FeignException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.dto.delivery.DeliveryUpdateRequestDto;
import shop.s5g.front.service.order.OrderService;
import shop.s5g.front.service.order.OrderDetailService;
import shop.s5g.front.annotation.RedirectWithAlert;
import shop.s5g.front.dto.order.OrderQueryRequestDto;
import shop.s5g.front.dto.order.OrderWithDetailResponseDto;

@RequiredArgsConstructor
@RequestMapping("/admin/order")
@Controller
public class AdminOrderController {
    private final shop.s5g.front.service.order.DeliveryService deliveryService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    // Primary Key인 customer id로 검색함. 비회원과 회원 모두 검색하게 하기 위함.
    @GetMapping
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

    @GetMapping("/{orderId}")
    public ModelAndView adminManageOrderDetail(@PathVariable long orderId) {
        ModelAndView mv = new ModelAndView("admin/order-detail");
        mv.addObject("info", orderDetailService.getOrderDetailAllInfos(orderId));
        return mv;
    }

    @RedirectWithAlert(exceptions = FeignException.class, title="", redirect = "/admin/order")
    @PostMapping("/delivery/{orderId}")
    public String adminUpdateOrderDetail(@PathVariable long orderId, DeliveryUpdateRequestDto updateRequest) {
        deliveryService.adminUpdateDelivery(updateRequest);
        return "redirect:/admin/order/" + orderId;
    }
}
