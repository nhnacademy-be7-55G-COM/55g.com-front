package shop.s5g.front.adapter;


import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.cart.request.CartLoginRequestDto;
import shop.s5g.front.dto.cart.request.CartPutRequestDto;
import shop.s5g.front.dto.cart.request.CartRemoveBookRequestDto;
import shop.s5g.front.dto.cart.request.CartUpdateQuantityRequestDto;


@FeignClient(value = "cart", url = "${gateway.url}")
public interface CartAdapter {

    @PostMapping("/api/shop/cart")
    ResponseEntity<MessageDto> putBook(@RequestBody CartPutRequestDto cartPutRequestDto);

    @GetMapping("/api/shop/cart")
    ResponseEntity<Map<String, Object>> getCartDetailPageInfo();

    @GetMapping("/api/shop/cart/guest")
    ResponseEntity<Map<String, Object>> getCartDetailPageInfoWhenGuest(
        @RequestParam("cartBookInfoList") String cartSessionStorageDto);


    @PostMapping("/api/shop/cart/login")
    ResponseEntity<Map<String,Integer>> convertCartToRedis(@RequestBody CartLoginRequestDto cartLoginRequestDto);

    @PostMapping("/api/shop/cart/logout")
    ResponseEntity<Void> redisToDbWhenLogout();

    @PostMapping("/api/shop/cart/controlQuantity")
    ResponseEntity<Void> updateQuantity(
        @RequestBody CartUpdateQuantityRequestDto cartUpdateQuantityRequestDto);

    @DeleteMapping("/api/shop/cart")
    ResponseEntity<Void> removeBook(@RequestBody CartRemoveBookRequestDto cartRemoveBookRequestDto);

}
