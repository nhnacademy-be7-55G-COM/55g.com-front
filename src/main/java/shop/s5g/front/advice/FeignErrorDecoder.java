package shop.s5g.front.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.BufferedInputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.exception.AlreadyExistsException;
import shop.s5g.front.exception.AuthenticationException;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.exception.ResourceNotFoundException;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 예외 리턴은 항상 MessageDto 라는 가정.
    @Override
    public Exception decode(String methodKey, Response response) {
        MessageDto messageDto = null;
        int status = response.status();
        try (BufferedInputStream in = new BufferedInputStream(response.body().asInputStream())){
            messageDto =objectMapper.readValue(in.readAllBytes(), MessageDto.class);
        } catch (IOException e) {
            log.error("FATAL ERROR: Feign Exception Message IOException", e);
            throw new IllegalAccessError(e.getMessage());
        }
        String url = response.request().requestTemplate().url();
        log.debug("Feign is handling error [url={}, status={}", url, status);
        return switch(status) {
            case 400 -> new BadRequestException(messageDto.message());
            case 404 -> new ResourceNotFoundException(messageDto.message());
            case 409 -> new AlreadyExistsException(messageDto.message());
            case 401 -> new AuthenticationException(messageDto.message());
            case 500 -> {
                log.error("Uncaught Error occurred on API: {}", url);
                yield defaultDecoder.decode(methodKey, response);
            }
            case 503 -> {
                log.error("Service not available. Check eureka or shop api server.");
                yield defaultDecoder.decode(methodKey, response);
            }
            default -> {
                log.error("Unknown Error");
                yield new IllegalArgumentException(String.valueOf(status));
            }
        };
    }
}
