package shop.s5g.front.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.util.WebUtils;
import shop.s5g.front.service.auth.AuthService;
import shop.s5g.front.utils.UserRoleHolder;

@RequiredArgsConstructor
public class AuthorizationFilter implements Filter {

    private final AuthService authService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Cookie cookie = WebUtils.getCookie(request, "accessJwt");
        if (cookie != null) {
            String token = cookie.getValue();
            String role = authService.getRole(token);
            UserRoleHolder.setRole(role);
        }
        else {
            UserRoleHolder.setRole("ANONYMOUS");
        }

        filterChain.doFilter(request, response);

        UserRoleHolder.free();
    }
}
