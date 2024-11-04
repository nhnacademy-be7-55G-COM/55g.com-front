package shop.S5G.front.service.member.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.S5G.front.adapter.MemberAdapter;
import shop.S5G.front.dto.jwt.TokenResponseDto;
import shop.S5G.front.dto.member.MemberLoginRequestDto;
import shop.S5G.front.dto.member.MemberRegistrationRequestDto;
import shop.S5G.front.dto.MessageDto;
import shop.S5G.front.exception.member.MemberLoginFailedException;
import shop.S5G.front.exception.member.MemberRegisterFailedException;
import shop.S5G.front.service.member.MemberService;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberAdapter memberAdapter;

    @Override
    public MessageDto registerMember(MemberRegistrationRequestDto memberRegistrationRequestDto) {
        try{
            ResponseEntity<MessageDto> response = memberAdapter.registerMember(memberRegistrationRequestDto);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new MemberRegisterFailedException("Member registration failed");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new MemberRegisterFailedException(e.getMessage());
        }

    }

    @Override
    public void loginMember(MemberLoginRequestDto memberLoginRequestDto, HttpServletResponse response) {
        try {
            ResponseEntity<TokenResponseDto> responseEntity = memberAdapter.loginMember(memberLoginRequestDto);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
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

                return;
            }
            throw new MemberLoginFailedException("Member login failed");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new MemberLoginFailedException(e.getMessage());
        }
    }
}
