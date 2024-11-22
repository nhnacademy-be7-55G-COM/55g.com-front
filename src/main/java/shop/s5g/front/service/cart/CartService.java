package shop.s5g.front.service.cart;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.book.BookPurchaseView;
import shop.s5g.front.dto.cart.request.CartBookInfoRequestDto;
import shop.s5g.front.dto.cart.request.CartLoginRequestDto;
import shop.s5g.front.dto.cart.request.CartPutRequestDto;
import shop.s5g.front.dto.cart.request.CartBookSelectRequestDto;
import shop.s5g.front.dto.cart.request.CartRemoveBookRequestDto;
import shop.s5g.front.dto.cart.request.CartUpdateQuantityRequestDto;

public interface CartService {

    int putBook(CartPutRequestDto cartPutRequestDto);

    Map<String, Object> getCartDetailPageInfo();

    Map<String, Object> getCartDetailPageInfoWhenGuest(String cartSessionStorage);

    Map<String, Integer> convertCartToRedis(CartLoginRequestDto cartLoginRequestDto);

    void redisToDbWhenLogout();

    void removeAccount();

    void updateQuantity(CartUpdateQuantityRequestDto cartUpdateQuantityRequestDto);

    void removeBook(CartRemoveBookRequestDto cartRemoveBookRequestDto);

    List<CartBookInfoRequestDto> getBooksWhenPurchase();

    CompletableFuture<List<BookPurchaseView>> convertCartToView(List<CartBookInfoRequestDto> cartList);

    void changeBookStatusInCart(CartBookSelectRequestDto cartBookSelectRequestDto);

    void removePurchasedBooks();
}
