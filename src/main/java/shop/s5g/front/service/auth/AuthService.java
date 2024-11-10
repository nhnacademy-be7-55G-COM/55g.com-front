package shop.s5g.front.service.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shop.s5g.front.dto.member.LoginRequestDto;

public interface AuthService {
    void loginMember(LoginRequestDto loginRequestDto, HttpServletResponse response);
    void loginAdmin(LoginRequestDto loginRequestDto, HttpServletResponse response);
    void logoutMember(HttpServletRequest request, HttpServletResponse response);
    void reissueToken(String refreshToken, HttpServletResponse response);
}
