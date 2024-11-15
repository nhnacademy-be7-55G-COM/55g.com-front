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
import shop.s5g.front.exception.AuthenticationException;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.exception.ResourceNotFoundException;
import shop.s5g.front.exception.cart.CartConvertException;
import shop.s5g.front.exception.cart.CartDetailPageException;
import shop.s5g.front.exception.cart.CartPutException;
import shop.s5g.front.exception.cart.CartRemoveAccountException;
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

        } catch (Exception e) {
            throw new CartPutException("물품을 담는데 실패했습니다.");
        }
    }

    @Override
    public Map<String, Object> getCartDetailPageInfo() {
        try {
            ResponseEntity<Map<String, Object>> cartDetailPageInfo = cartAdapter.getCartDetailPageInfo();

            return cartDetailPageInfo.getBody();

        } catch (Exception e) {
            throw new CartDetailPageException("장바구니 접근에 실패했습니다.");

        }

    }
    @Override
    public Map<String, Object> getCartDetailPageInfoWhenGuest(String cartSessionStorageDto) {
        try {
            ResponseEntity<Map<String, Object>> cartDetailPageInfo = cartAdapter.getCartDetailPageInfoWhenGuest(cartSessionStorageDto);

            return cartDetailPageInfo.getBody();

        } catch (Exception e) {
            throw new CartDetailPageException("장바구니 접근에 실패했습니다.");

        }

    }


    @Override
    public Map<String, Integer> convertCartToRedis(CartLoginRequestDto cartLoginRequestDto) {

        try {
            ResponseEntity<Map<String,Integer>> response = cartAdapter.convertCartToRedis(
                cartLoginRequestDto);

            return response.getBody();

        } catch (Exception e) {
            throw new CartConvertException("장바구니 초기화에 실패했습니다");
        }
    }

    @Override
    public void redisToDbWhenLogout() {
        try {
            cartAdapter.redisToDbWhenLogout();

        } catch (Exception e) {
            throw new CartConvertException("장바구니 초기화에 실패했습니다");
        }
    }
    @Override
    public void removeAccount() {
        try {
            cartAdapter.removeAccount();

        } catch (AuthenticationException e) {
            throw new CartRemoveAccountException("인증오류가 발생해 장바구니 정보삭제에 실패했습니다.");

        } catch (Exception e){
            throw new CartRemoveAccountException("장바구니 정보삭제에 실패했습니다.");
        }
    }

    @Override
    public void updateQuantity(CartUpdateQuantityRequestDto cartUpdateQuantityRequestDto) {


        try {
            cartAdapter.updateQuantity(cartUpdateQuantityRequestDto);

        } catch (Exception e) {
            throw new CartDetailPageException("수량 업데이트에 실패했습니다");

        }

    }

    @Override
    public void removeBook(CartRemoveBookRequestDto cartRemoveBookRequestDto) {
        try {
            cartAdapter.removeBook(cartRemoveBookRequestDto);

        } catch (Exception e) {
            throw new CartDetailPageException("해당 도서 삭제에 실패했습니다");
        }
    }
}
