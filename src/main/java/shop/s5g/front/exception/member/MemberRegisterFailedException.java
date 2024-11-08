package shop.s5g.front.exception.member;

public class MemberRegisterFailedException extends RuntimeException {

    public MemberRegisterFailedException(String message) {
        super(message);
    }
}
