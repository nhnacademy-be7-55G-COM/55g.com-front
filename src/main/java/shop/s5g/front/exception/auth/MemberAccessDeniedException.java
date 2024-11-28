package shop.s5g.front.exception.auth;

public class MemberAccessDeniedException extends RuntimeException {

    public MemberAccessDeniedException(String message) {
        super(message);
    }

    public MemberAccessDeniedException() {
        super("로그인이 필요합니다!");
    }
}
