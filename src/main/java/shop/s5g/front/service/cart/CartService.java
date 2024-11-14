package shop.s5g.front.service.cart;

import java.util.Map;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.cart.request.CartLoginRequestDto;
import shop.s5g.front.dto.cart.request.CartPutRequestDto;
import shop.s5g.front.dto.cart.request.CartRemoveBookRequestDto;
import shop.s5g.front.dto.cart.request.CartUpdateQuantityRequestDto;

public interface CartService {

    MessageDto putBook(CartPutRequestDto cartPutRequestDto);

    Map<String, Object> getCartDetailPageInfo();

    Map<String, Object> getCartDetailPageInfoWhenGuest(String cartSessionStorage);

    Map<String, Integer> convertCartToRedis(CartLoginRequestDto cartLoginRequestDto);

    void redisToDbWhenLogout();

    void updateQuantity(CartUpdateQuantityRequestDto cartUpdateQuantityRequestDto);

    void removeBook(CartRemoveBookRequestDto cartRemoveBookRequestDto);
}
