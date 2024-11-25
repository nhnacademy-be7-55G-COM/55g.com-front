package shop.s5g.front.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shop.s5g.front.interceptor.AdminAccessInterceptor;
import shop.s5g.front.interceptor.AuthorizationInterceptor;
import shop.s5g.front.interceptor.LoginStatusInterceptor;
import shop.s5g.front.interceptor.MemberAccessInterceptor;
import shop.s5g.front.interceptor.PurchaseSessionInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorizationInterceptor());
        registry.addInterceptor(new LoginStatusInterceptor());
        registry.addInterceptor(new PurchaseSessionInterceptor())
            .addPathPatterns("/purchase/**");
        registry.addInterceptor(new MemberAccessInterceptor()).addPathPatterns("/mypage/**");
        registry.addInterceptor(new AdminAccessInterceptor()).addPathPatterns("/admin/**")
            .excludePathPatterns("/admin/login");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error/access-denied").setViewName("error/access-denied");
    }
}
