package shop.s5g.front.domain.purchase;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import shop.s5g.front.dto.customer.CustomerResponseDto;
import shop.s5g.front.dto.delivery.DeliveryCreateRequestDto;
import shop.s5g.front.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.front.dto.member.MemberInfoResponseDto;
import shop.s5g.front.dto.order.OrderCreateRequestDto;
import shop.s5g.front.dto.order.OrderDetailCreateRequestDto;
import shop.s5g.front.exception.ApplicationException;
import shop.s5g.front.exception.ResourceNotFoundException;
import shop.s5g.front.service.member.MemberService;

@Component
@SessionScope                   // 세션에 종속적임
@RequiredArgsConstructor
@Slf4j
public class PurchaseSheet extends AbstractPurchaseSheet {    // 주문서 빈!
    private final transient MemberService memberService;

    @Getter
    private MemberInfoResponseDto memberInfo;       // 주문서를 작성하는 회원 정보

    @Getter @Setter(AccessLevel.PRIVATE)
    private BigDecimal accRateSum;          // 포인트 적립 비율 합산

    @Getter @Setter(AccessLevel.PRIVATE)
    private BigDecimal memberAccRate;       // 멤버십 포인트 비율

    // ------------------------------

    // 주문서를 생성했다면, 필요한 정보들을 비동기로 가져옴.
    @Override
    protected List<CompletableFuture<Void>> futuresSupplier() {
        return List.of(memberService.getMemberInfoAsync().thenAccept(
                info -> {
                    memberInfo = info;
                    setMemberAccRate(BigDecimal.valueOf(info.grade().point(), 2));
                }
            )
        );
    }

    // 상기의 비동기 작업들을 기다림.
    @Override
    protected void joinCallback() {
        sumAccRate();
    }

    private void sumAccRate() {     // 적립률 합연산
        memberAccRate = BigDecimal.valueOf(memberInfo.grade().point(), 2);
        setDefaultAccRate(policy.value());
        accRateSum = getDefaultAccRate().add(memberAccRate);
    }

    @Override
    public void pushToModel(ModelAndView mv) {      // 뷰에 표현하기 위한 DTO 주입.
        if (!isReady()) {
            log.error("Purchase Sheet was not joined! Please check synchronization logic!");
            throw new ApplicationException("PurchaseSheet does not ready");
        }
        log.trace("Attending arguments to purchase model...");
        mv.addObject("cartList", getCartList());
        mv.addObject("fees", getFee());
        mv.addObject("purchasePointPolicy", policy);
        mv.addObject("memberInfo", memberInfo);
        mv.addObject("accRate", accRateSum);
        mv.addObject("wrappingPaperList", getWraps().stream().map(wrappingPaperService::convertToView).toList());
        getCartList().forEach(cart -> orderInfo.purchaseMap.put(cart.id(), new PurchaseCell(cart)));
    }

    // shop api로 보낼 OrderCreateRequestDto를 작성함.
    @Override
    public OrderCreateRequestDto createOrderRequest(DeliveryCreateRequestDto delivery) {
        if (orderInfo == null) {
            throw new ApplicationException("OrderInfo is null, check order process");
        }
        long totalPrice = 0;
        long netPrice = 0;
        long accumulationPrice = 0;
        final long usePoint = orderInfo.usingPoint;
        List<OrderDetailCreateRequestDto> details = new ArrayList<>(getCartList().size());
        for (PurchaseCell cell: orderInfo.purchaseMap.values()) {
            BookPurchaseView book = cell.book;
            // TODO: 쿠폰 적용, 한 권만 적용
            long wrapCost = cell.wrappingPaper != null ? cell.wrappingPaper.price() : 0;
            long subTotalPrice = book.totalPrice() - 0 + (book.totalPrice() * (book.quantity()-1)) + wrapCost;
            int subAccPrice = usePoint == 0 ? BigDecimal.valueOf(subTotalPrice).multiply(accRateSum).intValue() : 0;
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

        // 최종 가격 결정.
        orderInfo.totalPrice = totalPrice - usePoint;
        orderInfo.netPrice = netPrice;

        // 배송비 유효성 체크
        boolean feeFlag = false;
        for (DeliveryFeeResponseDto f: getFee()) {
            if (orderInfo.totalPrice >= f.condition()) {
                if (delivery.deliveryFeeId() != f.id()) {
                    throw new IllegalArgumentException();
                }
                orderInfo.totalPrice += f.fee();
                feeFlag = true;
                break;
            }
        }
        if (!feeFlag) {
            log.error("Fatal Error: delivery fee is not available");
            throw new ResourceNotFoundException("적절한 배송비가 없음");
        }

        orderInfo.order = new OrderCreateRequestDto(
            memberInfo.customerId(), delivery, details, orderInfo.netPrice, orderInfo.totalPrice, usePoint
        );

        return orderInfo.order;
    }

    @Override
    public long updateUsingPoint(long point) {
        if (point < 0) {
            throw new UnsupportedOperationException("포인트는 음수가 될 수 없어요.");
        }
        if (memberInfo.point() < point) {
            throw new UnsupportedOperationException("포인트는 가지고 있는것보다 클 수 없어요.");
        }
        orderInfo.usingPoint = point;
        return point;
    }

    @Override
    public CustomerResponseDto getCustomerInfo() {
        return new CustomerResponseDto(
            memberInfo.customerId(), memberInfo.password(), memberInfo.name(), memberInfo.phoneNumber(), memberInfo.email()
        );
    }
}
