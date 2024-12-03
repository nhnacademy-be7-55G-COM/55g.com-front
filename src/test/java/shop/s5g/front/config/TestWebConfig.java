package shop.s5g.front.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shop.s5g.front.interceptor.LoginStatusInterceptor;

@TestConfiguration
@Import(LoginStatusInterceptor.class)
public class TestWebConfig implements WebMvcConfigurer {
    @Autowired
    LoginStatusInterceptor loginStatusInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginStatusInterceptor);
    }
}
