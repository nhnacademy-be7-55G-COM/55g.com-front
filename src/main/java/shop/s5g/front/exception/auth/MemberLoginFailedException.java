package shop.s5g.front.exception.auth;

public class MemberLoginFailedException extends RuntimeException {

    public MemberLoginFailedException(String message) {
        super(message);
    }
}
