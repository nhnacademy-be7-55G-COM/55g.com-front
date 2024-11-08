package shop.s5g.front.exception.member;

public class MemberUpdateFailedException extends RuntimeException {

    public MemberUpdateFailedException(String message) {
        super(message);
    }
}
