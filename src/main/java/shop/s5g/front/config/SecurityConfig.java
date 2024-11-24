package shop.s5g.front.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .exceptionHandling(customizer -> customizer.accessDeniedPage("/error/access-denied" ))
        ;

        http.cors(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
