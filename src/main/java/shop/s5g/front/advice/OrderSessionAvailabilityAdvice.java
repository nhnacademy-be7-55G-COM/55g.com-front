package shop.s5g.front.advice;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import shop.s5g.front.domain.purchase.PurchaseSheet;
import shop.s5g.front.exception.order.SessionDoesNotAvailableException;

@Aspect
@Order(2)
@Component
@RequiredArgsConstructor
public class OrderSessionAvailabilityAdvice {
    private final ObjectProvider<PurchaseSheet> sheetProvider;

    @Before("within(shop.s5g.front.controller.order.PaymentSupportController)")
    public void checkPurchaseSheetReady() {
        PurchaseSheet sheet = sheetProvider.getIfAvailable();
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
