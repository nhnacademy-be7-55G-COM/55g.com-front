package shop.s5g.front.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class PurchaseSessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            log.trace("Purchase Session is being removed...");
            session.invalidate();
        }
        log.trace("Purchase Session Created.");
        HttpSession newSession = request.getSession();
        newSession.setMaxInactiveInterval(1800);
        return true;
    }
}
