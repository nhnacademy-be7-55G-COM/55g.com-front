package shop.s5g.front.exception.auth;

public class AdminAccessDeniedException extends RuntimeException {

    public AdminAccessDeniedException(String message) {
        super(message);
    }
    public AdminAccessDeniedException() {
        super("권한이 없습니다.");
    }
}
