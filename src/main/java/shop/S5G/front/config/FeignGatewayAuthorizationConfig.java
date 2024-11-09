package shop.s5g.front.config;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import shop.s5g.front.utils.AuthTokenHolder;

@Slf4j
public class FeignGatewayAuthorizationConfig {
    @Bean
    public RequestInterceptor globalRequestInterceptor() {
        return requestTemplate -> {
            log.debug("Authorization token: {}", AuthTokenHolder.getToken());
            requestTemplate.header("Authorization", "Bearer " + AuthTokenHolder.getToken());
        };
    }
}
