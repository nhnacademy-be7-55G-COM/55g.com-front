package shop.s5g.front.exception.auth;

public class InactiveException extends RuntimeException {

    public InactiveException(String message) {
        super(message);
    }
}
