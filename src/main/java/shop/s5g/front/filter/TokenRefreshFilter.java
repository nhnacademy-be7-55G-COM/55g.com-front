package shop.s5g.front.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import shop.s5g.front.service.auth.AuthService;

@RequiredArgsConstructor
public class TokenRefreshFilter extends OncePerRequestFilter {

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        Cookie access = WebUtils.getCookie(request, "accessJwt");

        Cookie refresh = WebUtils.getCookie(request, "refreshJwt");

        if (access == null && refresh != null) {
            authService.reissueToken(refresh.getValue(), response);
            String uri = request.getRequestURI();
            response.sendRedirect(uri);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
