package shop.s5g.front.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import shop.s5g.front.exception.auth.AdminAccessDeniedException;
import shop.s5g.front.utils.UserRoleHolder;

@Slf4j
public class AdminAccessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        String role = UserRoleHolder.getRole();
        if (role.equals("ROLE_ADMIN")) {
            return true;
        }
        throw new AdminAccessDeniedException();
    }
}
