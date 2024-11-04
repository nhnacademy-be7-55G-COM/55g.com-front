package shop.S5G.front.exception.member;

public class MemberLoginFailedException extends RuntimeException {

    public MemberLoginFailedException(String message) {
        super(message);
    }
}
