package shop.s5g.front.service.member;

public interface InactiveService {
    String issueCode(String loginId);
    boolean validateCode(String loginId, int code);
}
