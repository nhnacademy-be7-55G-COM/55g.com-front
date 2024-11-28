package shop.s5g.front.controller.mypage;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.dto.order.OrderDetailInfoDto;
import shop.s5g.front.dto.order.OrderQueryRequestDto;
import shop.s5g.front.dto.order.OrderWithDetailResponseDto;
import shop.s5g.front.service.order.OrderDetailService;
import shop.s5g.front.service.order.OrderService;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageSubController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    @ModelAttribute("isGuest")
    public boolean isGuest() {
        return false;
    }

    @GetMapping("/point-history")
    public String viewPointHistory() {
        return "mypage/point-history";
    }

    /**
     * 주문 리스트를 보여주는 컨트롤러
     * @param startDate 서치 시작 날짜
     * @param endDate 서치 끝나는 날짜
     * @return 주문 리스트 뷰
     */
    @GetMapping("/order")
    public ModelAndView viewOrderList(
        @ModelAttribute @RequestParam(required = false) LocalDate startDate,
        @ModelAttribute @RequestParam(required = false) LocalDate endDate
    ) {
        ModelAndView mv = new ModelAndView("mypage/order-list");

        if (startDate == null || endDate == null) {
            LocalDate now = LocalDate.now();
            mv.addObject("orders", List.of());
            mv.addObject("startDate", now.minusMonths(1));
            mv.addObject("endDate", now);
        } else {
            List<OrderWithDetailResponseDto> orders = orderService.queryOrdersBetweenDates(
                new OrderQueryRequestDto(startDate, endDate)
            );
            mv.addObject("orders", orders);
        }

        return mv;
    }

    @GetMapping("/order/{uuid}")
    public ModelAndView viewOrderDetail(@PathVariable String uuid) {
        OrderDetailInfoDto info = orderDetailService.getOrderDetailAllInfos(uuid);
        ModelAndView mv = new ModelAndView("mypage/order-detail");

        // TODO: 리뷰 id 또는 리뷰 등록 여부 반환 필요 (리뷰 작성 여부 체크해야함) / boolean으로 반환되면 좋을듯

        mv.addObject("details", info.details());
        mv.addObject("delivery", info.delivery());
        mv.addObject("refunds", info.refunds());
        return mv;
    }
}
