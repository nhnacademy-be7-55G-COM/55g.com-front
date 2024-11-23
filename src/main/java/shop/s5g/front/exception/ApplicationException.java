package shop.s5g.front.exception;

// 범용 unchecked exception, INTERNAL SERVER ERROR 용도
// 주로 IOException 같은 checked exception을 재포장 하는데 사용.
public class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(Throwable throwable) {
      super(throwable);
    }
}
