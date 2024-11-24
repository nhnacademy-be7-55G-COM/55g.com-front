package shop.s5g.front.exception;

public class PrototypeBeanCreationFailedException extends ApplicationException{

    public PrototypeBeanCreationFailedException() {
        super("프로토타입 빈을 생성하는데 실패했습니다.");
    }
}
