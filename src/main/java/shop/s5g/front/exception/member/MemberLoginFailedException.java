package shop.s5g.front.exception.member;

public class MemberLoginFailedException extends RuntimeException {

    public MemberLoginFailedException(String message) {
        super(message);
    }
}
