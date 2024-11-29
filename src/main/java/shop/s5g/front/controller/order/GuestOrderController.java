package shop.s5g.front.controller.order;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.annotation.RedirectWithAlert;
import shop.s5g.front.dto.customer.GuestOrderQueryForm;
import shop.s5g.front.dto.order.OrderDetailInfoDto;
import shop.s5g.front.dto.order.OrderWithDetailResponseDto;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.exception.ResourceNotFoundException;
import shop.s5g.front.service.customer.CustomerService;
import shop.s5g.front.service.order.OrderDetailService;
import shop.s5g.front.service.order.OrderService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/guest")
public class GuestOrderController {
    private final CustomerService customerService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    @PostMapping("/order")
    @RedirectWithAlert(exceptions = ResourceNotFoundException.class, redirect = "/guest/login", title = "정보가 존재하지 않습니다.")
    @RedirectWithAlert(exceptions = BadRequestException.class, redirect = "/guest/login", title = "형식이 잘못되었습니다. 다시 시도해주세요.")
    public ModelAndView queryGuestOrders(GuestOrderQueryForm form) {
        String phoneNumber = String.format("010%s%s", form.phoneMiddle(), form.phoneEnd());
//        CustomerResponseDto customer = customerService.getCustomerInfoWithPN(phoneNumber, form.name(), form.password());

        List<OrderWithDetailResponseDto> orders = orderService.getGuestOrders(
            phoneNumber, form.name(), form.password()
        );
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("주문 정보가 존재하지 않습니다.");
        }

        ModelAndView mv = new ModelAndView("order/guest-order-list");
        mv.addObject("orderList", orders);

        return mv;
    }
    // TODO: 회원 것이랑 구분해야함.
    @GetMapping("/order/{orderId}")
    public ModelAndView viewOrderDetail(@PathVariable long orderId) {
        OrderDetailInfoDto info = orderDetailService.getOrderDetailAllInfos(orderId);
        ModelAndView mv = new ModelAndView("mypage/order-detail");

        // TODO: 리뷰 id 또는 리뷰 등록 여부 반환 필요 (리뷰 작성 여부 체크해야함) / boolean으로 반환되면 좋을듯

        mv.addObject("details", info.details());
        mv.addObject("delivery", info.delivery());
        mv.addObject("refunds", info.refunds());
        return mv;
    }
}
