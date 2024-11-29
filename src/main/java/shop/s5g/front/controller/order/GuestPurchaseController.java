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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.domain.purchase.GuestPurchaseSheet;
import shop.s5g.front.dto.cart.request.CartBookInfoRequestDto;
import shop.s5g.front.exception.cart.CartPurchaseException;
import shop.s5g.front.exception.order.PurchaseCreateErrorException;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/guest")
public class GuestPurchaseController {
    private final ObjectMapper objectMapper;
    private final GuestPurchaseSheet guestPurchaseSheet;

    private final TypeReference<List<CartBookInfoRequestDto>> typeReference = new TypeReference<List<CartBookInfoRequestDto>>() {};

    @GetMapping("/purchase/instant")
    public ModelAndView guestPurchaseView(@RequestParam("cart") String cartB64Encoded) {
        String rawCartList = new String(Base64.getUrlDecoder().decode(cartB64Encoded));
        ModelAndView mv = new ModelAndView("order/create-order-guest");

        log.trace("Getting guest shopping cart...");
        try {
            List<CartBookInfoRequestDto> bookListWhenPurchase = objectMapper.readValue(rawCartList, typeReference);
            if (bookListWhenPurchase.isEmpty()) {
                throw new PurchaseCreateErrorException("장바구니가 비어있습니다.");
            }
            guestPurchaseSheet
                .pushCartList(bookListWhenPurchase)
                .join();
            guestPurchaseSheet.pushToModel(mv);

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
