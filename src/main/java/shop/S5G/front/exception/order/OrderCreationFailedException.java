package shop.S5G.front.exception.order;

public class OrderCreationFailedException extends RuntimeException {

    public OrderCreationFailedException(String message) {
        super(message);
    }

}
