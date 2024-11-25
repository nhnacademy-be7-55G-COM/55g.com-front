package shop.s5g.front.domain.purchase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.dto.book.BookPurchaseView;
import shop.s5g.front.dto.customer.CustomerResponseDto;
import shop.s5g.front.dto.delivery.DeliveryCreateRequestDto;
import shop.s5g.front.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.front.dto.order.OrderCreateRequestDto;
import shop.s5g.front.dto.order.OrderDetailCreateRequestDto;
import shop.s5g.front.exception.ResourceNotFoundException;
import shop.s5g.front.service.customer.CustomerService;

@Slf4j
@SessionScope
@RequiredArgsConstructor
@Component
public class GuestPurchaseSheet extends AbstractPurchaseSheet{
    private final transient CustomerService customerService;

    private CustomerResponseDto customer = null;
    private boolean customerReady = false;

    @Override
    protected List<CompletableFuture<Void>> futuresSupplier() {
        return List.of();
    }

    @Override
    public void pushToModel(ModelAndView mv) {
        if (!isReady()) {
            log.error("Purchase Sheet was not joined! Please check synchronization logic!");
            throw new IllegalStateException();
        }
        mv.addObject("cartList", getCartList());
        mv.addObject("fees", getFee());
        mv.addObject("purchasePointPolicy", policy);
        mv.addObject("wrappingPaperList", getWraps().stream().map(wrappingPaperService::convertToView).toList());
        getCartList().forEach(cart -> orderInfo.purchaseMap.put(cart.id(), new PurchaseCell(cart)));
    }

//    @Override
//    public boolean isReady() {
//        return super.isReady() && customerReady;
//    }

    @Override
    public OrderCreateRequestDto createOrderRequest(DeliveryCreateRequestDto delivery) {
        if (orderInfo == null) {
            throw new IllegalStateException();
        }
        long totalPrice = 0;
        long netPrice = 0;
        List<OrderDetailCreateRequestDto> details = new ArrayList<>(getCartList().size());
        for (PurchaseCell cell: orderInfo.purchaseMap.values()) {
            BookPurchaseView book = cell.book;
            // TODO: 쿠폰 적용, 한 권만 적용
            long wrapCost = cell.wrappingPaper != null ? cell.wrappingPaper.price() : 0;
            long subTotalPrice = book.totalPrice() * book.quantity() + wrapCost;
            int subAccPrice = 0;
            // netPrice...
            totalPrice += subTotalPrice;

            details.add(new OrderDetailCreateRequestDto(
                book.id(),
                cell.wrappingPaper!=null ? cell.wrappingPaper.id() : null,
                book.quantity(),
                subTotalPrice,
                subAccPrice
            ));
        }

        // 최종 가격 결정.
        orderInfo.totalPrice = totalPrice;
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
            customer.customerId(), delivery, details, orderInfo.netPrice, orderInfo.totalPrice, 0
        );

        return orderInfo.order;
    }

    @Override
    public long updateUsingPoint(long point) {
        throw new UnsupportedOperationException("비회원은 포인트 기능을 제공하지 않습니다.");
    }

    @Override
    public CustomerResponseDto getCustomerInfo() {
        return customer;
    }

    public void setCustomer(CustomerResponseDto customer) {
        this.customer = customer;
        customerReady = true;
    }
}
