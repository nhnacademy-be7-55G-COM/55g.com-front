package shop.s5g.front.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.domain.PurchaseSheet;
import shop.s5g.front.dto.book.BookPurchaseView;
import shop.s5g.front.dto.book.BookSimpleResponseDto;
import shop.s5g.front.dto.cart.request.CartBookInfoRequestDto;
import shop.s5g.front.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.front.dto.member.MemberInfoResponseDto;
import shop.s5g.front.dto.point.PointPolicyView;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperResponseDto;
import shop.s5g.front.exception.ResourceNotFoundException;
import shop.s5g.front.service.book.BookService;
import shop.s5g.front.service.delivery.DeliveryFeeService;
import shop.s5g.front.service.member.MemberService;
import shop.s5g.front.service.point.PointPolicyService;
import shop.s5g.front.service.wrappingpaper.WrappingPaperService;

@RequiredArgsConstructor
@Slf4j
@Controller
public class PurchaseController {
    private final WrappingPaperService wrappingPaperService;
    private final MemberService memberService;
    private final PointPolicyService pointPolicyService;
    private final DeliveryFeeService deliveryFeeService;
    private final BookService bookService;
    private final PurchaseSheet purchaseSheet;

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
    public ModelAndView getPurchaseView(/* User Auth */ HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("create-order");
//        purchaseSheet.hello();

        log.trace("Getting shopping cart...");
        // 주문 세션의 시작.
        HttpSession session = request.getSession();
        // 최대 30분 동안 열림.
        session.setMaxInactiveInterval(1800);

        // TODO: 장바구니 가져오는 로직.
        //  장바구니가 비어있으면 장바구니로 리디렉트되도록 함.
        List<CartBookInfoRequestDto> rawCartList = List.of(
            new CartBookInfoRequestDto(1L, 2), new CartBookInfoRequestDto(3L, 1)
        );

        // 책 가져오는 로직.
        CompletableFuture<List<BookPurchaseView>> cartListFuture = CompletableFuture.supplyAsync(
                () -> convertCartToView(rawCartList)
        );
        // 포장지 가져오는 로직.
        CompletableFuture<List<WrappingPaperResponseDto>> wrapsFuture = wrappingPaperService.fetchActivePapersAsync();
        // 적립정책 가져오기.
        CompletableFuture<PointPolicyView> policyFuture = pointPolicyService.getPurchasePointPolicyAsync();
        // 멤버 정보 가져오기.
        CompletableFuture<MemberInfoResponseDto> memberInfoFuture = memberService.getMemberInfoAsync();
        // 배송비 가져오기.
        CompletableFuture<List<DeliveryFeeResponseDto>> feeFuture = deliveryFeeService.getAllFeesAsync();

        mv.addObject("wrappingPaperList", wrapsFuture.join().stream().map(wrappingPaperService::convertToView).toList());
        sumAccRate(mv, policyFuture.join(), memberInfoFuture.join());

        // 만약 결제할때 세션의 값을 사용한다면 멤버 등급이 바뀌거나 해서 적립률이 달라진다면 오차가 생기게 됨.

        mv.addObject("cartList", cartListFuture.join());
        mv.addObject("fees", feeFuture.join());

        return mv;
    }
    // TODO: 소수점 이슈때문에 나중에 각각 계산을 해야함.
    private BigDecimal sumAccRate(ModelAndView mv, PointPolicyView policy, MemberInfoResponseDto info) {
        mv.addObject("purchasePointPolicy", policy);
        mv.addObject("memberInfo", info);
        BigDecimal memberPoint = BigDecimal.valueOf(info.grade().point(), 2);
        BigDecimal rate = policy.value().add(memberPoint);
        mv.addObject("accRate", rate);

        return rate;
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
