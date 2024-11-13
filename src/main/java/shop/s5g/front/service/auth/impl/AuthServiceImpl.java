package shop.s5g.front.service.auth.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.util.WebUtils;
import shop.s5g.front.adapter.AuthAdapter;
import shop.s5g.front.dto.auth.RoleResponseDto;
import shop.s5g.front.dto.jwt.TokenResponseDto;
import shop.s5g.front.dto.member.LoginRequestDto;
import shop.s5g.front.exception.auth.LogoutException;
import shop.s5g.front.exception.auth.RoleGetInfoFailedException;
import shop.s5g.front.exception.auth.TokenNotFoundException;
import shop.s5g.front.exception.member.MemberLoginFailedException;
import shop.s5g.front.service.auth.AuthService;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthAdapter authAdapter;

    @Override
    public void loginMember(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        try {
            ResponseEntity<TokenResponseDto> responseEntity = authAdapter.loginMember(
                loginRequestDto);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                TokenResponseDto tokenResponseDto = responseEntity.getBody();

                if (tokenResponseDto != null) {
                    setTokenAtCookie(response, tokenResponseDto.accessToken(), tokenResponseDto.refreshToken(), 600, 1200);
                }
                return;
            }
            throw new MemberLoginFailedException("Member login failed");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new MemberLoginFailedException(e.getMessage());
        }
    }

    @Override
    public void loginAdmin(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        try {
            ResponseEntity<TokenResponseDto> responseEntity = authAdapter.loginAdmin(
                loginRequestDto);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                TokenResponseDto tokenResponseDto = responseEntity.getBody();

                if (tokenResponseDto != null) {
                    setTokenAtCookie(response, tokenResponseDto.accessToken(), tokenResponseDto.refreshToken(), 600, 1200);
                }
                return;
            }
            throw new MemberLoginFailedException("Member login failed");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new MemberLoginFailedException(e.getMessage());
        }
    }

    @Override
    public void logoutMember(HttpServletRequest request, HttpServletResponse response) {
        try{
            Cookie cookie = WebUtils.getCookie(request, "refreshJwt");
            if (cookie == null){
                throw new TokenNotFoundException("token not found");
            }
            String refreshToken = cookie.getValue();
            ResponseEntity<Void> responseEntity = authAdapter.logoutMember("Bearer " + refreshToken);

            if (responseEntity.getStatusCode().is2xxSuccessful()){
                Cookie accessJwt = new Cookie("accessJwt", null);
                accessJwt.setPath("/");
                accessJwt.setMaxAge(0);

                Cookie refreshJwt = new Cookie("refreshJwt" , null);
                refreshJwt.setPath("/");
                refreshJwt.setMaxAge(0);

                response.addCookie(accessJwt);
                response.addCookie(refreshJwt);
            }
        }
        //TODO finally로 감싸서 쿠키 삭제 강제? 만약 auth서버의 리프레시 토큰이 안지워진다면?
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new LogoutException("Logout failed");
        }
    }

    @Override
    public void reissueToken(String refreshToken, HttpServletResponse response) {
        try {
            ResponseEntity<TokenResponseDto> responseEntity = authAdapter.reissueToken("Bearer " + refreshToken);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    TokenResponseDto tokenResponseDto = responseEntity.getBody();

                    if (tokenResponseDto != null) {
                        setTokenAtCookie(response, tokenResponseDto.accessToken(), tokenResponseDto.refreshToken(), 600, 1200);
                    }
                    return;
                }
                throw new MemberLoginFailedException("Member login failed");

        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            setTokenAtCookie(response, null, null, 0 ,0);
            throw new MemberLoginFailedException(e.getMessage());
        }
    }

    @Override
    public String getRole(String accessToken) {
        try {
            ResponseEntity<RoleResponseDto> responseEntity = authAdapter.getRole("Bearer " + accessToken);
            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null){
                return responseEntity.getBody().role();
            }
            throw new RoleGetInfoFailedException("get role failed");

        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RoleGetInfoFailedException("get role failed");
        }
    }

    private void setTokenAtCookie(HttpServletResponse response,
        String accessToken, String refreshToken, int accessTokenExpiredTime, int refreshTokenExpiredTime) {

        Cookie accessJwt = new Cookie("accessJwt", accessToken);
        accessJwt.setPath("/");
        accessJwt.setMaxAge(accessTokenExpiredTime);
        accessJwt.setHttpOnly(true);
        response.addCookie(accessJwt);

        Cookie refreshJwt = new Cookie("refreshJwt", refreshToken);
        refreshJwt.setPath("/");
        refreshJwt.setMaxAge(refreshTokenExpiredTime);
        refreshJwt.setHttpOnly(true);
        response.addCookie(refreshJwt);

    }
}
