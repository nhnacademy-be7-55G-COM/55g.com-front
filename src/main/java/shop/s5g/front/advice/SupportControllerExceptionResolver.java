package shop.s5g.front.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.exception.AlreadyExistsException;
import shop.s5g.front.exception.ApplicationException;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.exception.ResourceNotFoundException;
import shop.s5g.front.exception.auth.UnauthorizedException;
import shop.s5g.front.exception.order.SessionDoesNotAvailableException;

@RestControllerAdvice
public class SupportControllerExceptionResolver {
    @ExceptionHandler(SessionDoesNotAvailableException.class)
    public ResponseEntity<HttpStatus> orderSessionNotAvailable(Exception e) {
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<HttpStatus> unauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler({BadRequestException.class, UnsupportedOperationException.class})
    public ResponseEntity<MessageDto> badRequestExceptionHandler(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<MessageDto> alreadyExistsExceptionHandler(AlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<MessageDto> resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler({ApplicationException.class})
    public ResponseEntity<MessageDto> applicationException(ApplicationException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageDto(e.getMessage()));
    }
}
