package shop.s5g.front.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.s5g.front.exception.auth.UnauthorizedException;
import shop.s5g.front.exception.order.OrderSessionNotAvailableException;

@RestControllerAdvice
public class SupportControllerExceptionResolver {
    @ExceptionHandler(OrderSessionNotAvailableException.class)
    public ResponseEntity<HttpStatus> orderSessionNotAvailable(Exception e) {
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<HttpStatus> unauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
