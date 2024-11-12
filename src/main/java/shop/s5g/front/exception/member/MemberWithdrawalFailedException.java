package shop.s5g.front.exception.member;

public class MemberWithdrawalFailedException extends RuntimeException {

    public MemberWithdrawalFailedException(String message) {
        super(message);
    }
}
