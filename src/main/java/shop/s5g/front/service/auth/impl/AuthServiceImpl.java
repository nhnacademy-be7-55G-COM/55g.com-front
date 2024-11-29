package shop.s5g.front.service.auth.impl;

import feign.FeignException;
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
import shop.s5g.front.exception.AuthenticationException;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.exception.auth.InactiveException;
import shop.s5g.front.exception.auth.LogoutException;
import shop.s5g.front.exception.auth.RoleGetInfoFailedException;
import shop.s5g.front.exception.auth.TokenNotFoundException;
import shop.s5g.front.exception.auth.MemberLoginFailedException;
import shop.s5g.front.service.auth.AuthService;
import shop.s5g.front.utils.TokenUtils;

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
                    TokenUtils.setTokenAtCookie(response, tokenResponseDto.accessToken(), tokenResponseDto.refreshToken(), 600, 1200);
                }
                return;
            }
            throw new MemberLoginFailedException("Member login failed");
        }
        catch (BadRequestException e){
            throw new InactiveException(e.getMessage());
        }
        catch (AuthenticationException | FeignException e){
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
                    TokenUtils.setTokenAtCookie(response, tokenResponseDto.accessToken(), tokenResponseDto.refreshToken(), 600, 1200);
                }
                return;
            }
            throw new MemberLoginFailedException("admin login failed");
        }
        catch (AuthenticationException | FeignException e){
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
        }
        catch (FeignException e) {
            throw new LogoutException("Logout failed");
        }
        finally {
            TokenUtils.setTokenAtCookie(response, null, null, 0 ,0);
        }
    }

    @Override
    public void reissueToken(String refreshToken, HttpServletResponse response) {
        try {
            ResponseEntity<TokenResponseDto> responseEntity = authAdapter.reissueToken("Bearer " + refreshToken);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    TokenResponseDto tokenResponseDto = responseEntity.getBody();

                    if (tokenResponseDto != null) {
                        TokenUtils.setTokenAtCookie(response, tokenResponseDto.accessToken(), tokenResponseDto.refreshToken(), 600, 1200);
                    }
                    return;
                }
                throw new MemberLoginFailedException("Member login failed");

        }
        catch (BadRequestException e) {
            TokenUtils.setTokenAtCookie(response, null, null, 0 ,0);
        }
        catch (FeignException e) {
            throw new MemberLoginFailedException(e.getMessage());
        }
    }

    @Override
    public String getRole(String accessToken) {
        try {
            ResponseEntity<RoleResponseDto> responseEntity = authAdapter.getRole("Bearer " + accessToken);
            if (responseEntity.getStatusCode().is2xxSuccessful()){
                RoleResponseDto responseDto = responseEntity.getBody();
                if (responseDto != null) {
                    return responseDto.role();
                }
            }
            throw new RoleGetInfoFailedException("get role failed");

        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RoleGetInfoFailedException("get role failed");
        }
    }

}
