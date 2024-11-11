package shop.s5g.front.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import shop.s5g.front.exception.auth.UnauthorizedException;
import shop.s5g.front.utils.UserRoleHolder;

@Aspect
@Component
public class AdminAuthorizationAspect {

    @Before("@within(shop.s5g.front.annotation.AdminOnly) || @annotation(shop.s5g.front.annotation.AdminOnly)")
    public void adminOnly() {
        String role = UserRoleHolder.getRole();
        if (!"ROLE_ADMIN".equals(role)) {
            throw new UnauthorizedException("Admin 권한이 없습니다");
        }
    }
}
