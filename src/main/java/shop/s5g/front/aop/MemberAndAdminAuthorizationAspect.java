package shop.s5g.front.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import shop.s5g.front.exception.auth.UnauthorizedException;

@Aspect
@Component
@RequiredArgsConstructor
@Order(3)
public class MemberAndAdminAuthorizationAspect {

    private final HttpServletRequest request;

    @Before("@within(shop.s5g.front.annotation.MemberAndAdminOnly) || " +
        "@annotation(shop.s5g.front.annotation.MemberAndAdminOnly)")
    public void checkMemberAccess() {
        Boolean isLoggedIn = (Boolean) request.getAttribute("isLoggedIn");
        if (isLoggedIn == null || !isLoggedIn) {
            throw new UnauthorizedException("로그인 하셔야 합니다");
        }
    }
}
