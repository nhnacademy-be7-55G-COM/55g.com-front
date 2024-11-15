package shop.s5g.front.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.s5g.front.utils.ApacheRandomStringUtilCaller;
import shop.s5g.front.utils.RandomStringProvider;

@Configuration
public class PaymentConfig {
    @Bean
    public String tossPaymentsClientKey() {
        // 클라이언트에서 노출되는 키
        return "test_ck_Z61JOxRQVEz1kKnJ6oMmrW0X9bAq";
    }

    @Bean
    public RandomStringProvider randomStringProvider() {
        return new ApacheRandomStringUtilCaller();
    }
}
