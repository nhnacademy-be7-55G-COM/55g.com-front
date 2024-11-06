package shop.S5G.front.service.auth.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.S5G.front.adapter.AuthAdapter;
import shop.S5G.front.dto.jwt.RefreshTokenRequestDto;
import shop.S5G.front.dto.jwt.TokenResponseDto;
import shop.S5G.front.dto.member.MemberLoginRequestDto;
import shop.S5G.front.exception.auth.TokenIssueFailedException;
import shop.S5G.front.exception.member.MemberLoginFailedException;
import shop.S5G.front.service.auth.AuthService;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthAdapter authAdapter;

    @Override
    public void loginMember(MemberLoginRequestDto memberLoginRequestDto, HttpServletResponse response) {
        try {
            ResponseEntity<TokenResponseDto> responseEntity = authAdapter.loginMember(memberLoginRequestDto);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                setCookieToken(responseEntity, response);
                return;
            }
            throw new MemberLoginFailedException("Member login failed");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new MemberLoginFailedException(e.getMessage());
        }
    }

    private void setCookieToken(ResponseEntity<TokenResponseDto> responseEntity, HttpServletResponse response) {
        TokenResponseDto tokenResponseDto = responseEntity.getBody();

        if (tokenResponseDto != null) {
            Cookie accessJwt = new Cookie("accessJwt" ,tokenResponseDto.accessToken());
            accessJwt.setPath("/");
            accessJwt.setMaxAge(3600);
            accessJwt.setHttpOnly(true);
            response.addCookie(accessJwt);

            Cookie refreshJwt = new Cookie("refreshJwt" ,tokenResponseDto.refreshToken());
            refreshJwt.setPath("/");
            refreshJwt.setMaxAge(3600);
            refreshJwt.setHttpOnly(true);
            response.addCookie(refreshJwt);
        }
    }
}
