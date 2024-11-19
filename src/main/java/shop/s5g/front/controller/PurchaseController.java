package shop.s5g.front.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.annotation.SessionRequired;
import shop.s5g.front.domain.purchase.PurchaseSheet;
import shop.s5g.front.dto.cart.request.CartBookInfoRequestDto;
import shop.s5g.front.exception.cart.CartPurchaseException;
import shop.s5g.front.service.cart.CartService;

@RequiredArgsConstructor
@Slf4j
@Controller
public class PurchaseController {
    private final PurchaseSheet purchaseSheet;
    private final CartService cartService;
    /**
     * 여기서 주문세션이 시작됨.
     * /purchase에 접근하면 주문세션이 시작(최대 1시간).
     * 결제버튼을 누르면 세션의 값을 가져와서 실제 payment의 결제와 동일한지 체크해야함.
     * 결제가 완료되었거나 실패하면 세션이 종료됨.
     * 장바구니로만 주문한다면 이럴 필요는 없지만,
     * 장바구니와 무관한 단품주문의 경우에 장바구니랑 다를 수 있으므로 세션을 열었음.
     * 다른 좋은 방법이 있을텐데..
     */
    @GetMapping("/purchase")
    @SessionRequired
    public ModelAndView getPurchaseView(/* User Auth */ HttpServletRequest request) {

        ModelAndView mv = new ModelAndView("create-order");

        log.trace("Getting shopping cart...");
        // TODO: 장바구니 가져오는 로직.

        try {
            List<CartBookInfoRequestDto> bookListWhenPurchase = cartService.getBooksWhenPurchase();

            purchaseSheet
                .pushCartList(bookListWhenPurchase)
                .join();
            purchaseSheet.pushToModel(mv);

            return mv;

        }catch (CartPurchaseException e){
            mv = new ModelAndView("cart/cartDetail");
            mv.addObject("PurchaseError", e.getMessage());

            return mv;
        }

        //  장바구니가 비어있으면 장바구니로 리디렉트되도록 함.
//        List<CartBookInfoRequestDto> rawCartList = List.of(
//            new CartBookInfoRequestDto(1L, 2), new CartBookInfoRequestDto(3L, 1)
//        );
    }


    @GetMapping("/purchase/test")
    @ResponseBody
    public String infoTest() {
        log.debug("MemberInfo: {}", purchaseSheet.getMemberInfo());
        log.debug("orderId: {}", purchaseSheet.getRandomOrderId());
        log.debug("orderInformation-pmap: {}", purchaseSheet.getOrderInfo().purchaseMap.toString());
        return "test";
    }
}
