package shop.s5g.front.advice;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import shop.s5g.front.domain.purchase.AbstractPurchaseSheet;
import shop.s5g.front.domain.purchase.GuestPurchaseSheet;
import shop.s5g.front.domain.purchase.PurchaseSheet;
import shop.s5g.front.exception.order.SessionDoesNotAvailableException;

@Aspect
@Order(2)
@Component
@RequiredArgsConstructor
public class OrderSessionAvailabilityAdvice {
    private final ObjectProvider<PurchaseSheet> purchaseSheetProvider;
    private final ObjectProvider<GuestPurchaseSheet> guestPurchaseSheetProvider;

    @Before("within(shop.s5g.front.controller.order.PaymentSupportController)")
    public void checkPurchaseSheetReady() {
        AbstractPurchaseSheet sheet = null;

        sheet = purchaseSheetProvider.getIfAvailable();
        if (sheet != null && sheet.isReady()) {
            return;
        }
        sheet = guestPurchaseSheetProvider.getIfAvailable();
        if (sheet != null && sheet.isReady()) {
            return;
        }

        throw SessionDoesNotAvailableException.INSTANCE;
    }
//    @Before("@annotation(shop.s5g.front.annotation.SessionRequired) || @within(shop.s5g.front.annotation.SessionRequired)")
//    public void sessionAvailabilityCheck(){
//        HttpServletRequest request =
//            (Objects.requireNonNull((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
//        HttpSession session = request.getSession(false);
//        if (session == null) {
//            throw SessionDoesNotAvailableException.INSTANCE;
//        }
//    }
}
