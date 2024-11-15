package shop.s5g.front.exception.order;

public class OrderCreationFailedException extends RuntimeException {

    public OrderCreationFailedException(String message) {
        super(message);
    }

}
