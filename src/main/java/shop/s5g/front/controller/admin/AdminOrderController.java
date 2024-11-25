package shop.s5g.front.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.dto.delivery.DeliveryUpdateRequestDto;
import shop.s5g.front.dto.order.OrderQueryFilterDto;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.service.delivery.DeliveryService;
import shop.s5g.front.service.order.OrderDetailService;
import shop.s5g.front.service.order.OrderService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/order")
@Controller
public class AdminOrderController {
    private final DeliveryService deliveryService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    // Primary Key인 customer id로 검색함. 비회원과 회원 모두 검색하게 하기 위함.
    @GetMapping
    public ModelAndView adminManageOrderQuery(OrderQueryFilterDto filter) {
        ModelAndView mv = new ModelAndView("admin/order-list");
        if (filter != null) {
            log.trace("filter: {}", filter);
            mv.addObject("orderList", orderService.adminQueryWithFilter(filter));
        }
        return mv;
    }

    @GetMapping("/{orderId}")
    public ModelAndView adminManageOrderDetail(@PathVariable long orderId) {
        ModelAndView mv = new ModelAndView("admin/order-detail");
        mv.addObject("info", orderDetailService.getOrderDetailAllInfos(orderId));
        return mv;
    }

//    @RedirectWithAlert(exceptions = FeignException.class, title="", redirect = "/admin/order")
    @PostMapping("/delivery/{orderId}")
    public String adminUpdateOrderDetail(
        @PathVariable long orderId,
        @Valid DeliveryUpdateRequestDto updateRequest,
        BindingResult errors
    ) {
        if (errors.hasErrors() || !updateRequest.selfValidation()) {
            throw new BadRequestException("배송 정보가 잘못되었습니다: " + errors.toString());
        }

        deliveryService.adminUpdateDelivery(updateRequest);
        return "redirect:/admin/order/" + orderId;
    }
}
