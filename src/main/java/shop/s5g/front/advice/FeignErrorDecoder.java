package shop.s5g.front.advice;

import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.BufferedInputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import shop.s5g.front.exception.AlreadyExistsException;
import shop.s5g.front.exception.AuthenticationException;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.exception.ResourceNotFoundException;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        String message = "";
        int status = response.status();
        try (BufferedInputStream in = new BufferedInputStream(response.body().asInputStream())){
            message = new String(in.readAllBytes());
        } catch (IOException e) {
            log.error("FATAL ERROR: Feign Exception Message IOException", e);
            throw new IllegalAccessError(e.getMessage());
        }
        String url = response.request().requestTemplate().url();
        log.debug("Feign is handling error [url={}, status={}", url, status);
        return switch(status) {
            case 400 -> new BadRequestException(message);
            case 404 -> new ResourceNotFoundException(message);
            case 409 -> new AlreadyExistsException(message);
            case 401 -> new AuthenticationException(message);
            case 500 -> {
                log.error("Uncaught Error occurred on API: {}", url);
                yield defaultDecoder.decode(methodKey, response);
            }
            default -> {
                log.error("Unknown Error");
                yield new IllegalArgumentException(String.valueOf(status));
            }
        };
    }
}
