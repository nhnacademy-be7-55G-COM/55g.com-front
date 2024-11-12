package shop.s5g.front.exception.book;

public class BookGetFailedException extends RuntimeException {
    public BookGetFailedException(String message) {
        super(message);
    }
}
