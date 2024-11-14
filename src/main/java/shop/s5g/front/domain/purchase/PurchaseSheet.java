package shop.s5g.front.domain.purchase;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import shop.s5g.front.dto.delivery.DeliveryCreateRequestDto;
import shop.s5g.front.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.front.dto.member.MemberInfoResponseDto;
import shop.s5g.front.dto.order.OrderCreateRequestDto;
import shop.s5g.front.dto.order.OrderDetailCreateRequestDto;
import shop.s5g.front.dto.point.PointPolicyView;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperResponseDto;
import shop.s5g.front.service.cart.CartService;
import shop.s5g.front.service.delivery.DeliveryFeeService;
import shop.s5g.front.service.member.MemberService;
import shop.s5g.front.service.point.PointPolicyService;
import shop.s5g.front.service.wrappingpaper.WrappingPaperService;
import shop.s5g.front.utils.RandomStringProvider;

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
    private final RandomStringProvider randomStringProvider;

    private List<CompletableFuture<?>> futures;

    private List<BookPurchaseView> cartList;
    private List<WrappingPaperResponseDto> wraps;
    private PointPolicyView policy;
    @Getter
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

    private OrderInformation orderInfo;
    // ------------------------------

    @PostConstruct
    public void init() {
        log.trace("---------- PurchaseSheet is initializing -----------");
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
        orderInfo = new OrderInformation(randomStringProvider);
        log.trace("PurchaseSheet has been initialized: {}", System.identityHashCode(this));
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
        mv.addObject("wrappingPaperList", wraps.stream().map(wrappingPaperService::convertToView).toList());
    }

    public String generateOrder() {
        orderInfo = new OrderInformation(randomStringProvider);
        cartList.forEach(cart -> orderInfo.purchaseMap.put(cart.id(), new PurchaseCell(cart)));
        return orderInfo.randomOrderId;
    }

    public String getRandomOrderId() {
        return orderInfo.randomOrderId;
    }

    public long getTotalPrice() {
        return orderInfo.totalPrice;
    }

    public long getNetPrice() {
        return orderInfo.netPrice;
    }

    public void setOrderId(long orderId) {
        orderInfo.orderId = orderId;
        orderInfo.ready = true;
    }

    public long getOrderId() {
        return orderInfo.orderId;
    }

    public OrderCreateRequestDto createOrderRequest(DeliveryCreateRequestDto delivery) {
        if (orderInfo == null) {
            throw new IllegalStateException();
        }
        long totalPrice = 0;
        long netPrice = 0;
        long accumulationPrice = 0;
        List<OrderDetailCreateRequestDto> details = new ArrayList<>(cartList.size());
        for (PurchaseCell cell: orderInfo.purchaseMap.values()) {
            BookPurchaseView book = cell.book;
            // TODO: 쿠폰 적용, 한 권만 적용
            long subTotalPrice = book.totalPrice() - 0 + (book.totalPrice() * (book.quantity()-1));
            int subAccPrice = BigDecimal.valueOf(subTotalPrice).multiply(accRateSum).intValue();
            // netPrice...
            totalPrice += subTotalPrice;
            accumulationPrice += subAccPrice;
            details.add(new OrderDetailCreateRequestDto(
                book.id(),
                cell.wrappingPaper!=null ? cell.wrappingPaper.id() : null,
                book.quantity(),
                subTotalPrice,
                subAccPrice
            ));
        }

        orderInfo.totalPrice = totalPrice;
        orderInfo.netPrice = netPrice;

        orderInfo.order = new OrderCreateRequestDto(
            memberInfo.customerId(), delivery, details, netPrice, totalPrice
        );

        return orderInfo.order;
    }
}
