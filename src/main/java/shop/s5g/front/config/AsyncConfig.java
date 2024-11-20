package shop.s5g.front.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import shop.s5g.front.decorator.TaskThreadLocalDecorator;

@EnableAsync
@Configuration
public class AsyncConfig {
    @Bean("purchaseExecutor")
    public Executor purchaseRequestAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(300);
        executor.setThreadNamePrefix("PurchaseExec-");
        executor.setTaskDecorator(new TaskThreadLocalDecorator());
        executor.initialize();
        return executor;
    }
}
