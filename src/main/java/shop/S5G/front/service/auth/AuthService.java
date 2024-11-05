package shop.S5G.front.service.auth;

import jakarta.servlet.http.HttpServletResponse;
import shop.S5G.front.dto.member.MemberLoginRequestDto;

public interface AuthService {
    void loginMember(MemberLoginRequestDto memberLoginRequestDto, HttpServletResponse response);
}
