package shop.s5g.front.controller.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.annotation.MemberAndAdminOnly;
import shop.s5g.front.annotation.RedirectWithAlert;
import shop.s5g.front.annotation.SessionRequired;
import shop.s5g.front.domain.purchase.PurchaseSheet;
import shop.s5g.front.dto.cart.request.CartBookInfoRequestDto;
import shop.s5g.front.exception.cart.CartPurchaseException;
import shop.s5g.front.exception.order.PurchaseCreateErrorException;
import shop.s5g.front.service.cart.CartService;

@RequiredArgsConstructor
@Slf4j
@Controller
@MemberAndAdminOnly
@RedirectWithAlert(exceptions = PurchaseCreateErrorException.class, redirect = "/", title = "주문서 생성에 실패했습니다.")
public class PurchaseController {
    private final PurchaseSheet purchaseSheet;
    private final CartService cartService;
    private final ObjectMapper objectMapper;
    private final TypeReference<List<CartBookInfoRequestDto>> typeReference = new TypeReference<List<CartBookInfoRequestDto>>() {};
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
    public ModelAndView getPurchaseView() {
        ModelAndView mv = new ModelAndView("create-order");

        log.trace("Getting shopping cart...");
        try {
            List<CartBookInfoRequestDto> bookListWhenPurchase = cartService.getBooksWhenPurchase();
            if (bookListWhenPurchase.isEmpty()) {
                throw new PurchaseCreateErrorException("장바구니가 비어있습니다.");
            }
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
    }
    @GetMapping("/purchase/instant")
    @SessionRequired
    public ModelAndView instantPurchaseSheet(@RequestParam("cart") String cartB64Encoded) {
        String rawCartList = new String(Base64.getUrlDecoder().decode(cartB64Encoded));

        ModelAndView mv = new ModelAndView("create-order");

        log.trace("Getting shopping cart...");
        try {
            List<CartBookInfoRequestDto> bookListWhenPurchase = objectMapper.readValue(rawCartList, typeReference);
            if (bookListWhenPurchase.isEmpty()) {
                throw new PurchaseCreateErrorException("장바구니가 비어있습니다.");
            }
            purchaseSheet
                .pushCartList(bookListWhenPurchase)
                .join();
            purchaseSheet.pushToModel(mv);

            return mv;
        } catch (CartPurchaseException e){
            mv = new ModelAndView("cart/cartDetail");
            mv.addObject("PurchaseError", e.getMessage());

            return mv;
        } catch (JsonProcessingException e) {
            log.debug("Json Processing Exception, [origin={}, rawList={}]", cartB64Encoded, rawCartList);
            throw new PurchaseCreateErrorException("장바구니 형식이 잘못되었습니다.");
        }
    }
}
