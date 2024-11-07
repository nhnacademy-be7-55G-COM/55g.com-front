package shop.S5G.front.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shop.S5G.front.interceptor.AuthorizationInterceptor;
import shop.S5G.front.interceptor.LoginStatusInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorizationInterceptor());
        registry.addInterceptor(new LoginStatusInterceptor()).addPathPatterns("/**");
    }
}
