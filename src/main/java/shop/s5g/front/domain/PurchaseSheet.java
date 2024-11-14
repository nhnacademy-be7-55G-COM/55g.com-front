package shop.s5g.front.domain;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.dto.book.BookPurchaseView;
import shop.s5g.front.dto.cart.request.CartBookInfoRequestDto;
import shop.s5g.front.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.front.dto.member.MemberInfoResponseDto;
import shop.s5g.front.dto.point.PointPolicyView;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperResponseDto;
import shop.s5g.front.service.cart.CartService;
import shop.s5g.front.service.delivery.DeliveryFeeService;
import shop.s5g.front.service.member.MemberService;
import shop.s5g.front.service.point.PointPolicyService;
import shop.s5g.front.service.wrappingpaper.WrappingPaperService;

@Component
@SessionScope
@RequiredArgsConstructor
@Slf4j
public class PurchaseSheet {
    private final MemberService memberService;
    private final PointPolicyService pointPolicyService;
    private final DeliveryFeeService deliveryFeeService;
    private final WrappingPaperService wrappingPaperService;
    private final CartService cartService;

    private List<CompletableFuture<?>> futures;

    private List<BookPurchaseView> cartList;
    private List<WrappingPaperResponseDto> wraps;
    private PointPolicyView policy;
    private MemberInfoResponseDto memberInfo;
    private List<DeliveryFeeResponseDto> fee;

    @Getter @Setter(AccessLevel.PRIVATE)
    private BigDecimal accRateSum;

    @Getter @Setter(AccessLevel.PRIVATE)
    private BigDecimal memberAccRate;

    @Getter @Setter(AccessLevel.PRIVATE)
    private BigDecimal defaultAccRate;

    private boolean cartReady;
    private boolean joined;

    @PostConstruct
    public void init() {
        log.trace("---------- PurchaseSheet initialized -----------");
        futures = new LinkedList<>();
        futures.add(pointPolicyService.getPurchasePointPolicyAsync().thenAccept(
            view -> {
                policy = view;
                setDefaultAccRate(view.value());
            }
        ));
        futures.add(memberService.getMemberInfoAsync().thenAccept(
            info -> {
                memberInfo = info;
                setMemberAccRate(BigDecimal.valueOf(info.grade().point(), 2));
            }
        ));
        futures.add(wrappingPaperService.fetchActivePapersAsync().thenAccept(
            papers -> wraps = papers
        ));
        futures.add(deliveryFeeService.getAllFeesAsync().thenAccept(
            fees-> fee = fees
        ));
        cartReady = false;
        joined = false;
    }
    public PurchaseSheet pushCartList(List<CartBookInfoRequestDto> rawCartList) {
        futures.add(
            cartService.convertCartToView(rawCartList)
                .thenAccept(cart -> {
                    cartList = cart;
                    cartReady = true;
                }
            )
        );
        return this;
    }

    public void join() {
        CompletableFuture<?>[] arrFuture = futures.toArray(new CompletableFuture[0]);
        CompletableFuture.allOf(arrFuture).thenRun(
            () -> {
                sumAccRate();
                setJoined();
                log.trace("-------- PurchaseSheet Joined ----------");
            }
        ).join();
    }

    public boolean isReady() {
        return joined && cartReady;
    }

    private void setJoined() {
        joined = true;
    }

    // TODO: 소수점 이슈때문에 나중에 각각 계산을 해야함.
    private void sumAccRate() {
        memberAccRate = BigDecimal.valueOf(memberInfo.grade().point(), 2);
        defaultAccRate = policy.value();
        accRateSum = defaultAccRate.add(memberAccRate);
    }

    // TODO: 적절한 예외로 바꾸기
    public void pushToModel(ModelAndView mv) {
        if (!isReady()) {
            log.error("Purchase Sheet was not joined! Please check synchronization logic!");
            throw new IllegalStateException();
        }
        log.trace("Attending arguments to purchase model...");
        mv.addObject("cartList", cartList);
        mv.addObject("fees", fee);
        mv.addObject("purchasePointPolicy", policy);
        mv.addObject("memberInfo", memberInfo);
        mv.addObject("accRate", accRateSum);
    }
}
