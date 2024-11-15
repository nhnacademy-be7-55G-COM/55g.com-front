package shop.s5g.front.exception.auth;

public class RoleGetInfoFailedException extends RuntimeException {
    public RoleGetInfoFailedException() {
        super();
    }
    public RoleGetInfoFailedException(String message) {
        super(message);
    }
}
