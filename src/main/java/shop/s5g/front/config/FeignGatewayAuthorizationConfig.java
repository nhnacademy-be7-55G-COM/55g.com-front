package shop.s5g.front.config;

import feign.Client;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import feign.httpclient.ApacheHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import shop.s5g.front.advice.FeignErrorDecoder;
import shop.s5g.front.utils.AuthTokenHolder;

@Slf4j
public class FeignGatewayAuthorizationConfig {
    @Bean
    public RequestInterceptor globalRequestInterceptor() {
        return requestTemplate -> {
            log.debug("Authorization token: {}", AuthTokenHolder.getToken());
            if (!requestTemplate.headers().containsKey("Authorization")) {
                requestTemplate.header("Authorization", "Bearer " + AuthTokenHolder.getToken());
            } else {
                log.debug("Authorization header already exists, skipping.");
            }
        };
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

    @Bean
    public CloseableHttpClient closeableHttpClient() {
        return HttpClients.createDefault();
    }
    @Bean
    public Client feignClient(CloseableHttpClient httpClient) {
        return new ApacheHttpClient(httpClient);
    }
}
