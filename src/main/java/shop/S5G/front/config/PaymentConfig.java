package shop.S5G.front.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {
    @Bean
    public String tossPaymentClientKey() {
        // 클라이언트에서 노출되는 키
        return "test_ck_Z61JOxRQVEz1kKnJ6oMmrW0X9bAq";
    }
}
