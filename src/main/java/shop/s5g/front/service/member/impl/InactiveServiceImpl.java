package shop.s5g.front.service.member.impl;

import java.time.Duration;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import shop.s5g.front.service.member.InactiveService;
import shop.s5g.front.utils.RandomNumberUtils;

@Service
@RequiredArgsConstructor
public class InactiveServiceImpl implements InactiveService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String INACTIVE_KEY = "inactive:";

    @Override
    public String issueCode(String loginId) {
        String number = String.valueOf(RandomNumberUtils.generateFourDigitNumber());
        redisTemplate.opsForValue().set(INACTIVE_KEY + loginId, number, Duration.ofSeconds(300));
        return number;
    }

    @Override
    public boolean validateCode(String loginId, int code) {
        String number = Objects.requireNonNull(
            redisTemplate.opsForValue().get(INACTIVE_KEY + loginId)).toString();
        if (number == null) {
            return false;
        }

        return Integer.parseInt(number) == code;
    }
}
