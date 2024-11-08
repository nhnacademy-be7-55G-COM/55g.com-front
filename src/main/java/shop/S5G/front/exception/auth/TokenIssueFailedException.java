package shop.S5G.front.exception.auth;

public class TokenIssueFailedException extends RuntimeException {

    public TokenIssueFailedException(String message) {
        super(message);
    }
}
