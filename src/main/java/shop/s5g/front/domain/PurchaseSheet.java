package shop.s5g.front.domain;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import shop.s5g.front.dto.book.BookPurchaseView;
import shop.s5g.front.dto.book.BookSimpleResponseDto;
import shop.s5g.front.dto.cart.request.CartBookInfoRequestDto;
import shop.s5g.front.exception.ResourceNotFoundException;
import shop.s5g.front.service.book.BookService;
import shop.s5g.front.service.delivery.DeliveryFeeService;
import shop.s5g.front.service.member.MemberService;
import shop.s5g.front.service.point.PointPolicyService;
import shop.s5g.front.utils.AuthTokenHolder;
import shop.s5g.front.utils.FunctionalWithAuthToken;

@Component
@SessionScope
@RequiredArgsConstructor
@Slf4j
public class PurchaseSheet {
    private final MemberService memberService;
    private final PointPolicyService pointPolicyService;
    private final DeliveryFeeService deliveryFeeService;
    private final BookService bookService;

    private List<CompletableFuture<?>> futures;

    private List<BookPurchaseView> cartList;
    @Getter @Setter
    private BigDecimal accRateSum;

    @Getter @Setter
    private BigDecimal memberAccRate;

    @Getter @Setter
    private BigDecimal defaultAccRate;

    private boolean cartReady;

    @Getter
    private boolean joined;

    @PostConstruct
    public void init() {
        log.error("PurchaseSheet initialized");
        futures = new LinkedList<>();
        futures.add(pointPolicyService.getPurchasePointPolicyAsync().thenAccept(
            view -> setDefaultAccRate(view.value())
        ));
        futures.add(memberService.getMemberInfoAsync().thenAccept(
            info -> setMemberAccRate(BigDecimal.valueOf(info.grade().point(), 2))
        ));

//        CompletableFuture<MemberInfoResponseDto> memberInfoFuture;
        cartReady = false;
        joined = false;
    }
    public void pushCartList(List<CartBookInfoRequestDto> rawCartList) {
        futures.add(
            CompletableFuture.supplyAsync(
                FunctionalWithAuthToken.supply(
                    AuthTokenHolder.getToken(),
                    () -> convertCartToView(rawCartList)
                )
            ).thenAccept(cart -> {
                    cartList = cart;
                    cartReady = true;
                }
            )
        );
    }

    public void join() {
        CompletableFuture<?>[] arrFuture = futures.toArray(new CompletableFuture[0]);
        CompletableFuture.allOf(arrFuture).thenRun(this::setJoined);
    }

    public void hello(){
        log.error("hello");
    }
    public boolean isReady() {
        return joined && cartReady;
    }

    private void setJoined() {
        joined = true;
    }
    private List<BookPurchaseView> convertCartToView(List<CartBookInfoRequestDto> cartList) {
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
        return bookView;
    }
}
