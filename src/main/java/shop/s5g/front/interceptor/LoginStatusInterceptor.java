package shop.s5g.front.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

public class LoginStatusInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        Cookie refreshJwt = WebUtils.getCookie(request, "refreshJwt");

        request.setAttribute("isLoggedIn", refreshJwt != null);
        return true;
    }
}
