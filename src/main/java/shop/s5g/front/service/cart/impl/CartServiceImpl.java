package shop.s5g.front.service.cart.impl;


import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.CartAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.cart.request.CartLoginRequestDto;
import shop.s5g.front.dto.cart.request.CartPutRequestDto;
import shop.s5g.front.dto.cart.request.CartRemoveBookRequestDto;
import shop.s5g.front.dto.cart.request.CartUpdateQuantityRequestDto;
import shop.s5g.front.exception.cart.CartConvertException;
import shop.s5g.front.exception.cart.CartDetailPageException;
import shop.s5g.front.exception.cart.CartPutException;
import shop.s5g.front.service.cart.CartService;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartAdapter cartAdapter;

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
}
