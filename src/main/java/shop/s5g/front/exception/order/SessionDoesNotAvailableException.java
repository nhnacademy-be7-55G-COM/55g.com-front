package shop.s5g.front.exception.order;

public class SessionDoesNotAvailableException extends RuntimeException {
    public static final SessionDoesNotAvailableException INSTANCE =
        new SessionDoesNotAvailableException("세션이 만료되었거나 존재하지 않습니다. 다시 시도해 주세요.");

    public SessionDoesNotAvailableException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
