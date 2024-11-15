package shop.s5g.front.exception.auth;

public class TokenIssueFailedException extends RuntimeException {

    public TokenIssueFailedException(String message) {
        super(message);
    }
}
