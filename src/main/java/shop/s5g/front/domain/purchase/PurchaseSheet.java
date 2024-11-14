package shop.s5g.front.domain.purchase;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import shop.s5g.front.dto.order.WrapModifyRequestDo;
import shop.s5g.front.dto.point.PointPolicyView;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperResponseDto;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperView;
import shop.s5g.front.service.cart.CartService;
import shop.s5g.front.service.delivery.DeliveryFeeService;
import shop.s5g.front.service.member.MemberService;
import shop.s5g.front.service.point.PointPolicyService;
import shop.s5g.front.service.wrappingpaper.WrappingPaperService;
import shop.s5g.front.utils.RandomStringProvider;

@Component
@SessionScope                   // 세션에 종속적임
@RequiredArgsConstructor
@Slf4j
public class PurchaseSheet {    // 주문서 빈!
    private final MemberService memberService;
    private final PointPolicyService pointPolicyService;
    private final DeliveryFeeService deliveryFeeService;
    private final WrappingPaperService wrappingPaperService;
    private final CartService cartService;
    private final RandomStringProvider randomStringProvider;

    // 비동기 작업을 관리하는 리스트
    private List<CompletableFuture<?>> futures;

    private List<BookPurchaseView> cartList;        // 카트에서 가져온 책들
    private List<WrappingPaperResponseDto> wraps;   // 사용 가능한 포장지들
    private PointPolicyView policy;                 // 기본 포인트 정책
    @Getter
    private MemberInfoResponseDto memberInfo;       // 주문서를 작성하는 회원 정보
    // TODO: 아직 배송비가 포함되지 않음.
    private List<DeliveryFeeResponseDto> fee;       // 배송비 리스트

    @Getter @Setter(AccessLevel.PRIVATE)
    private BigDecimal accRateSum;          // 포인트 적립 비율 합산

    @Getter @Setter(AccessLevel.PRIVATE)
    private BigDecimal memberAccRate;       // 멤버십 포인트 비율

    @Getter @Setter(AccessLevel.PRIVATE)
    private BigDecimal defaultAccRate;      // 기본 포인트 적립 비율

    private boolean cartReady;              // 카트에 책정보를 다 가져왔는지?
    private boolean joined;                 // futures 의 작업이 다 끝났는지?
    private Map<Long, WrappingPaperView> wrapMap;   // 각 책마다 포장지 선택을 저장함.

    private OrderInformation orderInfo;     // 주문 정보
    // ------------------------------

    // 주문서를 생성했다면, 필요한 정보들을 비동기로 가져옴.
    @PostConstruct
    public void init() {
        log.trace("---------- PurchaseSheet is initializing -----------");
        futures = new LinkedList<>();
        wrapMap = new HashMap<>();
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
            papers -> {
                wraps = papers;
                wraps.stream().map(wrappingPaperService::convertToView).forEach(
                    w-> wrapMap.put(w.id(), w)
                );
            }
        ));
        futures.add(deliveryFeeService.getAllFeesAsync().thenAccept(
            fees-> fee = fees
        ));
        cartReady = false;
        joined = false;
        orderInfo = new OrderInformation(randomStringProvider);
        log.trace("PurchaseSheet has been initialized: {}", System.identityHashCode(this));
    }
    // 쇼핑카트에서 책 ID를 가져와서 책 정보를 요청함.
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

    // 상기의 비동기 작업들을 기다림.
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
    // 상기의 비동기 작업들이 끝났는지?
    public boolean isReady() {
        return joined && cartReady;
    }
    // 비동기 작업이 완료되었음!
    private void setJoined() {
        joined = true;
    }

    // TODO: 소수점 이슈때문에 나중에 각각 계산을 해야함.
    private void sumAccRate() {     // 적립률 합연산
        memberAccRate = BigDecimal.valueOf(memberInfo.grade().point(), 2);
        defaultAccRate = policy.value();
        accRateSum = defaultAccRate.add(memberAccRate);
    }

    // TODO: 적절한 예외로 바꾸기
    public void pushToModel(ModelAndView mv) {      // 뷰에 표현하기 위한 DTO 주입.
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
        cartList.forEach(cart -> orderInfo.purchaseMap.put(cart.id(), new PurchaseCell(cart)));
    }

    public String generateOrder() { // 이전 버전에서 order 넘버를 생성하던 메소드.
//        orderInfo = new OrderInformation(randomStringProvider);
        return orderInfo.randomOrderId;
    }

    // TODO: 위 메소드와 동일함. 리팩토링 해야함.
    public String getRandomOrderId() {
        return orderInfo.randomOrderId;
    }

    public long getTotalPrice() {   // 총 결제 금액을 리턴
        return orderInfo.totalPrice;
    }

    public long getNetPrice() {     // netPrice가 뭐였더라...
        return orderInfo.netPrice;
    }

    public void setOrderId(long orderId) {  // orderId를 세팅함.
        orderInfo.orderId = orderId;
        orderInfo.ready = true;
    }

    public long getOrderId() {  // orderId를 가져옴.
        return orderInfo.orderId;
    }

    // shop api로 보낼 OrderCreateRequestDto를 작성함.
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
            long wrapCost = cell.wrappingPaper != null ? cell.wrappingPaper.price() : 0;
            long subTotalPrice = book.totalPrice() - 0 + (book.totalPrice() * (book.quantity()-1)) + wrapCost;
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

    // 포장지를 바꿀때 마다 호출해주는 메소드.
    // 바꾼 결과를 purchaseMap에 저장함.
    public void changeWrappingPaper(WrapModifyRequestDo wrapModify) {
        Map<Long, PurchaseCell> purchaseMap = orderInfo.purchaseMap;
       PurchaseCell cell = purchaseMap.get(wrapModify.bookId());

       if (wrapModify.wrapId() == -1) {
           cell.wrappingPaper = null;
       } else {
           cell.wrappingPaper = wrapMap.get(wrapModify.wrapId());
       }

    }
}
