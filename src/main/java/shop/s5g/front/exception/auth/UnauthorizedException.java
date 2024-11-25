package shop.s5g.front.exception.auth;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException() {
        super("권한이 없습니다!");
    }
}
