package shop.s5g.front.exception.member;

public class PasswordChangeFailedException extends RuntimeException {

    public PasswordChangeFailedException(String message) {
        super(message);
    }
}
