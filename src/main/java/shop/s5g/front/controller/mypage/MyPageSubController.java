package shop.s5g.front.controller.mypage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.dto.order.OrderDetailInfoDto;
import shop.s5g.front.service.order.OrderDetailService;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageSubController {
    private final OrderDetailService orderDetailService;

    @GetMapping("/point-history")
    public String viewPointHistory() {
        return "mypage/point-history";
    }

    @GetMapping("/order")
    public String viewOrderList() {
        return "mypage/order-list";
    }

    @GetMapping("/order/{orderId}")
    public ModelAndView viewOrderDetail(@PathVariable long orderId) {
        OrderDetailInfoDto info = orderDetailService.getOrderDetailAllInfos(orderId);
        ModelAndView mv = new ModelAndView("mypage/order-detail");

        mv.addObject("details", info.details());
        mv.addObject("delivery", info.delivery());
        mv.addObject("refunds", info.refunds());
        return mv;
    }
}
