package shop.s5g.front.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Objects;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import shop.s5g.front.exception.order.OrderSessionNotAvailableException;

@Aspect
@Order(2)
@Component
public class SessionAvailabilityAdvice {
    @Before("@annotation(shop.s5g.front.annotation.SessionRequired)")
    public void sessionAvailabilityCheck(){
        HttpServletRequest request =
            (Objects.requireNonNull((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw OrderSessionNotAvailableException.INSTANCE;
        }
    }
}
