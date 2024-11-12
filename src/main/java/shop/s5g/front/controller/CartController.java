package shop.s5g.front.controller;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner.Mode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.cart.request.CartLoginRequestDto;
import shop.s5g.front.dto.cart.request.CartPutRequestDto;
import shop.s5g.front.dto.cart.request.CartRemoveBookRequestDto;
import shop.s5g.front.dto.cart.request.CartUpdateQuantityRequestDto;
import shop.s5g.front.exception.cart.CartDetailPageException;
import shop.s5g.front.service.cart.CartService;

@Controller
@RequiredArgsConstructor
public class CartController {


    private final CartService cartService;

    @GetMapping("/cart/loginProcess")
    public String cartLoginProcessing() {
        return "cart/cartLogin";
    }

    @GetMapping("/cart/loginCheck")
    public ResponseEntity<Map<String,Boolean>> cartLoginCheck(@CookieValue(value = "accessJwt",required = false) String isLoggedIn) {

        if (Objects.isNull(isLoggedIn)){
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("isLoggedIn",false));
        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("isLoggedIn",true));
    }

    // 로그인할 때 세션스토리지에 저장되었던 물품들을 레디스로 옮기고 회원 로그인상태에서는 레디스를 이용
    @PostMapping("/cart/login")
    public ResponseEntity<Map<String,Integer>> convertCartToRedis(@RequestBody @Validated CartLoginRequestDto cartLoginRequestDto) {
        Map<String, Integer> cartCount = cartService.convertCartToRedis(cartLoginRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(cartCount);
    }

    @PostMapping("/cart")
    public String putBook(@RequestBody @Validated CartPutRequestDto cartPutRequestDto) {

        MessageDto messageDto = cartService.putBook(cartPutRequestDto);

        return messageDto.message();
    }

    @GetMapping("/cart/detailPage")
    public String cartDetailPage(Model model, RedirectAttributes redirectAttributes,
        @RequestParam(value = "cartBookInfoList", required = false) String cartSessionStorage,
        HttpServletResponse response) {

        if (cartSessionStorage == null) {
            // 로그인한 경우
            try {
                cartDetailPageWhenLogin(response, model);

                return "cart/cartDetail";

            } catch (CartDetailPageException e) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

                return "redirect:/";
            }
        } else {
            // 로그인 하지 않은 경우
            try {
                cartDetailPageWhenGuest(cartSessionStorage, response, model);
                return "cart/cartDetail";

            } catch (CartDetailPageException e) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

                return "redirect:/";
            }
        }
    }

    @PostMapping("/cart/updateQuantity")
    public ResponseEntity<?> updateQuantity(@RequestBody CartUpdateQuantityRequestDto cartUpdateQuantityReq) {
        try {
            cartService.updateQuantity(cartUpdateQuantityReq);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (CartDetailPageException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/cart/removeBook")
    public ResponseEntity<?> removeBook(@RequestBody CartRemoveBookRequestDto cartRemoveBookReq) {
        try {
            cartService.removeBook(cartRemoveBookReq);

            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (CartDetailPageException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }

    }

    private void cartDetailPageWhenLogin(HttpServletResponse response, Model model) {
        Map<String, Object> cartDetailPageInfo = cartService.getCartDetailPageInfo(); // 로그인 상태라면 api 에 가서 장바구니 정보를 가져와야한다.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        model.addAttribute("books", cartDetailPageInfo.get("books"));
        model.addAttribute("feeInfo", cartDetailPageInfo.get("feeInfo"));
    }

    private void cartDetailPageWhenGuest(String cartSessionStorage, HttpServletResponse response,
        Model model) {
        Map<String, Object> cartDetailPageInfo = cartService.getCartDetailPageInfoWhenGuest(cartSessionStorage);

        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        model.addAttribute("books", cartDetailPageInfo.get("books"));
        model.addAttribute("feeInfo", cartDetailPageInfo.get("feeInfo"));

    }
}
