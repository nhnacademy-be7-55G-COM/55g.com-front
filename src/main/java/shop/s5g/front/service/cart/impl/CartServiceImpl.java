package shop.s5g.front.service.cart.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.CartAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.book.BookPurchaseView;
import shop.s5g.front.dto.book.BookSimpleResponseDto;
import shop.s5g.front.dto.cart.request.CartBookInfoRequestDto;
import shop.s5g.front.dto.cart.request.CartLoginRequestDto;
import shop.s5g.front.dto.cart.request.CartPutRequestDto;
import shop.s5g.front.dto.cart.request.CartRemoveBookRequestDto;
import shop.s5g.front.dto.cart.request.CartUpdateQuantityRequestDto;
import shop.s5g.front.exception.ResourceNotFoundException;
import shop.s5g.front.exception.cart.CartConvertException;
import shop.s5g.front.exception.cart.CartDetailPageException;
import shop.s5g.front.exception.cart.CartPutException;
import shop.s5g.front.service.book.BookService;
import shop.s5g.front.service.cart.CartService;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartAdapter cartAdapter;
    private final BookService bookService;

    @Override
    public MessageDto putBook(CartPutRequestDto cartPutRequestDto) {
        try {
            ResponseEntity<MessageDto> response = cartAdapter.putBook(cartPutRequestDto);

            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e instanceof HttpClientErrorException) {
                throw new CartPutException("요청이 올바르지 않습니다");
            }else{
                throw new CartPutException("서버 오류가 발생했습니다.");
            }
        }
    }

    @Override
    public Map<String, Object> getCartDetailPageInfo() {
        try {
            ResponseEntity<Map<String, Object>> cartDetailPageInfo = cartAdapter.getCartDetailPageInfo();

            return cartDetailPageInfo.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e instanceof HttpClientErrorException) {
                throw new CartDetailPageException("요청이 올바르지 않습니다");
            }else{
                throw new CartDetailPageException("서버 오류가 발생했습니다.");
            }

        }

    }
    @Override
    public Map<String, Object> getCartDetailPageInfoWhenGuest(String cartSessionStorageDto) {
        try {
            ResponseEntity<Map<String, Object>> cartDetailPageInfo = cartAdapter.getCartDetailPageInfoWhenGuest(cartSessionStorageDto);

            return cartDetailPageInfo.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e instanceof HttpClientErrorException) {
                throw new CartDetailPageException("요청이 올바르지 않습니다");
            }else{
                throw new CartDetailPageException("서버 오류가 발생했습니다.");
            }

        }

    }



    @Override
    public Map<String, Integer> convertCartToRedis(CartLoginRequestDto cartLoginRequestDto) {

        try {
            ResponseEntity<Map<String,Integer>> response = cartAdapter.convertCartToRedis(
                cartLoginRequestDto);

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CartConvertException("장바구니 초기화에 실패했습니다");
        }
    }
    @Override
    public void redisToDbWhenLogout() {
        try {
            ResponseEntity<Void> response = cartAdapter.redisToDbWhenLogout();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CartConvertException("장바구니 초기화에 실패했습니다");
        }
    }

    @Override
    public void updateQuantity(CartUpdateQuantityRequestDto cartUpdateQuantityRequestDto) {


        try {
            ResponseEntity<Void> response = cartAdapter.updateQuantity(
                cartUpdateQuantityRequestDto);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e instanceof HttpClientErrorException) {
                throw new CartDetailPageException("요청이 올바르지 않습니다");
            }else{
                throw new CartDetailPageException("서버 오류가 발생했습니다.");
            }

        }

    }

    @Override
    public void removeBook(CartRemoveBookRequestDto cartRemoveBookRequestDto) {
        try {
            ResponseEntity<Void> response = cartAdapter.removeBook(cartRemoveBookRequestDto);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e instanceof HttpClientErrorException) {
                throw new CartDetailPageException("요청이 올바르지 않습니다");
            }else{
                throw new CartDetailPageException("서버 오류가 발생했습니다.");
            }

        }
    }

    @Async("purchaseRequest")
    @Override
    public CompletableFuture<List<BookPurchaseView>> convertCartToView(List<CartBookInfoRequestDto> cartList) {
        List<BookSimpleResponseDto> bookList = bookService.getSimpleBooksFromCart(cartList);

        if (bookList.size() != cartList.size()) {
            throw new ResourceNotFoundException("카트에 담긴 책이 존재하지 않음.");
        }
        List<BookPurchaseView> bookView = new ArrayList<>(cartList.size());
        for (int i=0; i<cartList.size(); i++) {
            BookSimpleResponseDto book = bookList.get(i);
            BigDecimal rate = book.discountRate();
            CartBookInfoRequestDto cart = cartList.get(i);
            long price = book.price();
            long discountPrice = BigDecimal.valueOf(price).multiply(rate).longValue();
            bookView.add(new BookPurchaseView(book.id(), book.title(), price, cart.quantity(), discountPrice, price - discountPrice));
        }
        return CompletableFuture.completedFuture(bookView);
    }
}
