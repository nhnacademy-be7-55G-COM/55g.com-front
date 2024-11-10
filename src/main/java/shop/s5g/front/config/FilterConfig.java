package shop.s5g.front.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.s5g.front.filter.LoginStatusInterceptor;
import shop.s5g.front.filter.TokenRefreshFilter;
import shop.s5g.front.service.auth.AuthService;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final AuthService authService;

    @Bean
    public FilterRegistrationBean<TokenRefreshFilter> tokenRefreshFilter() {
        FilterRegistrationBean<TokenRefreshFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TokenRefreshFilter(authService));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
