package shop.s5g.front.exception.order;

public class OrderSessionNotAvailableException extends RuntimeException {
    public static final OrderSessionNotAvailableException INSTANCE =
        new OrderSessionNotAvailableException("주문 세션이 만료되었거나 존재하지 않습니다. 다시 시도해 주세요.");

    public OrderSessionNotAvailableException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
