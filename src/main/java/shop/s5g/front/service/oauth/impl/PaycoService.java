package shop.s5g.front.service.oauth.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.PaycoAdapter;
import shop.s5g.front.dto.jwt.TokenResponseDto;
import shop.s5g.front.exception.auth.TokenIssueFailedException;
import shop.s5g.front.exception.member.MemberLoginFailedException;
import shop.s5g.front.service.oauth.OauthService;
import shop.s5g.front.utils.TokenUtils;

@Service
@RequiredArgsConstructor
public class PaycoService implements OauthService {

    private final PaycoAdapter paycoAdapter;

    @Override
    public void getToken(String code, HttpServletResponse response) {
        try{
            ResponseEntity<TokenResponseDto> responseEntity = paycoAdapter.getToken(code);


            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                TokenResponseDto tokenResponseDto = responseEntity.getBody();

                if (tokenResponseDto != null) {
                    TokenUtils.setTokenAtCookie(response, tokenResponseDto.accessToken(), tokenResponseDto.refreshToken(), 600, 1200);
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
