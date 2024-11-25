package shop.s5g.front.domain.purchase;

import jakarta.annotation.PostConstruct;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.dto.book.BookPurchaseView;
import shop.s5g.front.dto.cart.request.CartBookInfoRequestDto;
import shop.s5g.front.dto.customer.CustomerResponseDto;
import shop.s5g.front.dto.delivery.DeliveryCreateRequestDto;
import shop.s5g.front.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.front.dto.order.OrderCreateRequestDto;
import shop.s5g.front.dto.order.WrapModifyRequestDo;
import shop.s5g.front.dto.point.PointPolicyView;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperResponseDto;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperView;
import shop.s5g.front.service.cart.CartService;
import shop.s5g.front.service.delivery.DeliveryFeeService;
import shop.s5g.front.service.point.PointPolicyService;
import shop.s5g.front.service.wrappingpaper.WrappingPaperService;
import shop.s5g.front.utils.RandomStringProvider;

@Slf4j
@SessionScope
public abstract class AbstractPurchaseSheet implements Serializable {
    @Setter(onMethod_ = {@Autowired})
    protected transient PointPolicyService pointPolicyService;
    @Setter(onMethod_ = {@Autowired})
    protected transient DeliveryFeeService deliveryFeeService;
    @Setter(onMethod_ = {@Autowired})
    protected transient WrappingPaperService wrappingPaperService;
    @Setter(onMethod_ = {@Autowired})
    protected transient CartService cartService;
    @Setter(onMethod_ = {@Autowired})
    protected transient RandomStringProvider randomStringProvider;

    // 어차피 불변이라 protected 여도 상관없음.
    protected PointPolicyView policy;                 // 기본 포인트 정책
    // 비동기 작업을 관리하는 리스트
    @Getter
    private transient List<CompletableFuture<Void>> futures;
    @Getter
    private List<BookPurchaseView> cartList;        // 카트에서 가져온 책들\
    @Getter
    private List<WrappingPaperResponseDto> wraps;   // 사용 가능한 포장지들
    @Getter
    private LinkedList<DeliveryFeeResponseDto> fee;       // 배송비 리스트
    private boolean cartReady;              // 카트에 책정보를 다 가져왔는지?

    private boolean joined;                 // futures 의 작업이 다 끝났는지?

    private HashMap<Long, WrappingPaperView> wrapMap;   // 각 책마다 포장지 선택을 저장함.
    @Getter @Setter
    private boolean isOrdering;

    // TODO: 일단 protected 인데... 리팩토링 하느라 캡슐화에 한참 위배되게 만듬.
    //  나중에 시간나면 수정할 것.
    protected OrderInformation orderInfo;     // 주문 정보

    @Getter @Setter(AccessLevel.PROTECTED)
    private BigDecimal defaultAccRate;      // 기본 포인트 적립 비율

    // ------------------------------

    // 주문서를 생성했다면, 필요한 정보들을 비동기로 가져옴.
    // final 메소드면 Cglib 가 프록시를 못함.. 상속 기반 프록시이기 때문에..
    @PostConstruct
    public void initialize() {
        log.trace("---------- PurchaseSheet is initializing -----------");
        futures = new LinkedList<>();
        wrapMap = new HashMap<>();

        futures.add(pointPolicyService.getPurchasePointPolicyAsync().thenAccept(
            view -> {
                policy = view;
                setDefaultAccRate(view.value());
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
            fees-> {
                fee = fees; // 배송비 조건이 큰 순서부터 정렬
                fee.sort((c1, c2) -> Math.toIntExact((int) c2.condition() - c1.condition()));
            }
        ));
        log.trace("-------------- Custom Future Started ---------------");
        futures.addAll(futuresSupplier());
        cartReady = false;
        joined = false;
        orderInfo = new OrderInformation(randomStringProvider);
        log.trace("PurchaseSheet has been initialized: {}", System.identityHashCode(this));
    }

    protected abstract List<CompletableFuture<Void>> futuresSupplier();

    // 쇼핑카트에서 책 ID를 가져와서 책 정보를 요청함.
    public AbstractPurchaseSheet pushCartList(List<CartBookInfoRequestDto> rawCartList) {
        futures.add(cartService.convertCartToView(rawCartList)
            .thenAccept(cart -> {
                cartList = cart;
                cartReady = true;
            })
        );
        return this;
    }

    // 상기의 비동기 작업들을 기다림.
    public void join() {
        CompletableFuture<Void>[] arrFuture = getFutures().toArray(new CompletableFuture[0]);
        CompletableFuture.allOf(arrFuture).thenRun(
            () -> {
                joinCallback();
                setJoined();
                log.trace("-------- PurchaseSheet Joined ----------");
            }
        ).join();
    }

    // 서브 클래스가 등록하는 join 콜백
    protected void joinCallback() {}

    // 상기의 비동기 작업들이 끝났는지?
    public boolean isReady() {
        return joined && cartReady;
    }

    // 비동기 작업이 완료되었음!
    protected void setJoined() {
        joined = true;
    }

    // TODO: 적절한 예외로 바꾸기
    public abstract void pushToModel(ModelAndView mv);

    // TODO: 위 메소드와 동일함. 리팩토링 해야함.
    public String getRandomOrderId() {
        return orderInfo.randomOrderId;
    }

    public long getTotalPrice() {   // 총 결제 금액을 리턴
        return orderInfo.totalPrice;
    }

    public long getUsedPoint() {
        return orderInfo.usingPoint;
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
    public abstract OrderCreateRequestDto createOrderRequest(DeliveryCreateRequestDto delivery);

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

    public abstract long updateUsingPoint(long point);
    public abstract CustomerResponseDto getCustomerInfo();
}
