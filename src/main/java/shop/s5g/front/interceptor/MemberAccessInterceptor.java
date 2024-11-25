package shop.s5g.front.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import shop.s5g.front.exception.auth.MemberAccessDeniedException;
import shop.s5g.front.utils.UserRoleHolder;

public class MemberAccessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        String role = UserRoleHolder.getRole();
        if (role.equals("ROLE_MEMBER")) {
            return true;
        }
        throw new MemberAccessDeniedException();
    }
}
